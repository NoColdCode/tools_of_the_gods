# Smoke-test Fabric runClient for each MC version (one at a time).
# Success: log contains mod id and no crash before timeout.
param(
    [int]$TimeoutSeconds = 240,
    [string[]]$Versions = @('1.16.5', '1.18.2', '1.19.2', '1.20.1', '1.21.1', '1.21.8', '26.1')
)

$root = Split-Path -Parent $PSScriptRoot
Push-Location $root

$results = @()

function Invoke-GradleQuiet {
    param([string[]]$GradleArgs)
    $prev = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    $logFile = Join-Path $env:TEMP ("tog-smoke-build-" + [guid]::NewGuid().ToString('N') + ".log")
    & .\gradlew.bat @GradleArgs *> $logFile
    $code = $LASTEXITCODE
    if ($code -ne 0 -and (Test-Path $logFile)) {
        Get-Content $logFile -Tail 20 -ErrorAction SilentlyContinue | ForEach-Object { Write-Host $_ -ForegroundColor DarkYellow }
    }
    Remove-Item $logFile -Force -ErrorAction SilentlyContinue
    $ErrorActionPreference = $prev
    return $code
}

foreach ($v in $Versions) {
    Write-Host "`n========== fabric-$v ==========" -ForegroundColor Cyan

    Write-Host "Building..."
    $buildCode = Invoke-GradleQuiet @(":fabric-${v}:build", '--no-daemon', '-PtogLoader=fabric', "-PfabricVersions=$v")
    if ($buildCode -ne 0) {
        $results += [pscustomobject]@{ Version = $v; Build = 'FAIL'; Client = 'SKIP'; Note = "build exit $buildCode" }
        Write-Host "BUILD FAILED - skipping runClient" -ForegroundColor Red
        continue
    }
    Write-Host "BUILD OK" -ForegroundColor Green

    $runDir = Join-Path $root "fabric\$v\run"
    $logFile = Join-Path $runDir "logs\latest.log"
    $modsDir = Join-Path $runDir "mods"
    if (Test-Path $logFile) { Remove-Item $logFile -Force -ErrorAction SilentlyContinue }
    if (Test-Path $modsDir) { Remove-Item (Join-Path $modsDir "*") -Force -ErrorAction SilentlyContinue }

    Write-Host "Starting runClient (timeout ${TimeoutSeconds}s)..."
    $proc = Start-Process -FilePath ".\gradlew.bat" -ArgumentList @(
        ":fabric-${v}:runClient", "--no-daemon", "-PtogLoader=fabric", "-PfabricVersions=$v"
    ) -PassThru -WorkingDirectory $root -WindowStyle Hidden

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    $clientOk = $false
    $reason = 'timeout'

    while ((Get-Date) -lt $deadline) {
        if ($proc.HasExited -and -not $clientOk) {
            $reason = "process exited ($($proc.ExitCode))"
            break
        }
        if (Test-Path $logFile) {
            $log = Get-Content $logFile -Raw -ErrorAction SilentlyContinue
            if ($log -match 'Game crashed!|CrashReport saved to:|Block id not set|duplicate fabric loader|Incompatible mods found') {
                $reason = 'crash in log'
                break
            }
            if ($log -match 'tools_of_the_gods' -and ($log -match 'Loaded \d+ recipes' -or $log -match 'OpenAL initialized' -or $log -match 'Created: \d+x\d+' -or $log -match 'Tools of the Gods .+ loaded on Fabric')) {
                $clientOk = $true
                $reason = 'client reached main menu / recipes loaded'
                Start-Sleep -Seconds 3
                break
            }
        }
        Start-Sleep -Seconds 5
    }

    try { taskkill /PID $proc.Id /T /F 2>$null | Out-Null } catch {}
    Get-Process -Name javaw,java -ErrorAction SilentlyContinue | Where-Object {
        $_.MainWindowTitle -match 'Minecraft|NeoForge|Forge|Fabric' -or $_.Path -match 'tools_of_the_gods'
    } | ForEach-Object { Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue }

    $status = if ($clientOk) { 'PASS' } else { 'FAIL' }
    $color = if ($clientOk) { 'Green' } else { 'Red' }
    Write-Host "CLIENT $status ($reason)" -ForegroundColor $color
    $results += [pscustomobject]@{ Version = $v; Build = 'OK'; Client = $status; Note = $reason }
}

Pop-Location

Write-Host "`n========== SUMMARY ==========" -ForegroundColor Cyan
$results | Format-Table -AutoSize

if ($results | Where-Object { $_.Build -ne 'OK' -or $_.Client -ne 'PASS' }) { exit 1 }
