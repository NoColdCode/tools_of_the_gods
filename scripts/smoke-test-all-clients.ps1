# Smoke-test runClient for every loader/version.
# Usage: .\scripts\smoke-test-all-clients.ps1 [-TimeoutSeconds 300]
param(
    [int]$TimeoutSeconds = 300
)

$ErrorActionPreference = 'Continue'
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

$targets = @(
    @{ Loader = 'neoforge'; Versions = @('1.21.1', '1.21.8', '26.1') },
    @{ Loader = 'fabric';   Versions = @('1.16.5', '1.18.2', '1.19.2', '1.20.1', '1.21.1', '1.21.8', '26.1') },
    @{ Loader = 'forge';    Versions = @('1.18.2', '1.19.2', '1.20.1') }
)

$results = [System.Collections.Generic.List[object]]::new()
$logPath = Join-Path $root 'smoke-all-results.txt'
"Smoke started $(Get-Date -Format o)" | Out-File -FilePath $logPath -Encoding utf8

function Write-Log([string]$msg, [string]$color = 'White') {
    Write-Host $msg -ForegroundColor $color
    Add-Content -Path $logPath -Value $msg -Encoding utf8
}

function Invoke-Gradle([string[]]$GradleArgs) {
    $logFile = Join-Path $env:TEMP ("tog-smoke-build-" + [guid]::NewGuid().ToString('N') + ".log")
    & .\gradlew.bat @GradleArgs *> $logFile
    $code = $LASTEXITCODE
    if ($code -ne 0 -and (Test-Path $logFile)) {
        Get-Content $logFile -Tail 25 -ErrorAction SilentlyContinue | ForEach-Object { Write-Log $_ 'DarkYellow' }
    }
    Remove-Item $logFile -Force -ErrorAction SilentlyContinue
    return $code
}

function Stop-ClientTree([System.Diagnostics.Process]$proc) {
    if ($null -eq $proc) { return }
    try { taskkill /PID $proc.Id /T /F 2>$null | Out-Null } catch {}
}

foreach ($group in $targets) {
    $loader = $group.Loader
    $prop = "${loader}Versions"
    foreach ($v in $group.Versions) {
        $label = "${loader}-$v"
        Write-Log "`n========== $label ==========" 'Cyan'

        Write-Log "Building..."
        $buildCode = Invoke-Gradle @(":${label}:build", '--no-daemon', "-PtogLoader=$loader", "-P${prop}=$v")
        if ($buildCode -ne 0) {
            Write-Log "BUILD FAILED (exit $buildCode)" 'Red'
            $results.Add([pscustomobject]@{ Target = $label; Build = 'FAIL'; Client = 'SKIP'; Note = "build exit $buildCode" })
            continue
        }
        Write-Log "BUILD OK" 'Green'

        $runDir = Join-Path $root "$loader\$v\run"
        $logFile = Join-Path $runDir 'logs\latest.log'
        $modsDir = Join-Path $runDir 'mods'
        if (Test-Path $logFile) { Remove-Item $logFile -Force -ErrorAction SilentlyContinue }
        if (Test-Path $modsDir) { Remove-Item (Join-Path $modsDir '*') -Force -ErrorAction SilentlyContinue }

        Write-Log "Starting runClient (timeout ${TimeoutSeconds}s)..."
        $proc = Start-Process -FilePath '.\gradlew.bat' -ArgumentList @(
            ":${label}:runClient", '--no-daemon', "-PtogLoader=$loader", "-P${prop}=$v"
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
                if ($log -match 'Game crashed!|CrashReport saved to:|Block id not set|Incompatible mods found|Failed to load mod|duplicate fabric loader') {
                    $reason = 'crash in log'
                    break
                }
                if ($log -match 'tools_of_the_gods' -and (
                    $log -match 'Loaded \d+ recipes' -or
                    $log -match 'OpenAL initialized' -or
                    $log -match 'Created: \d+x\d+' -or
                    $log -match 'Tools of the Gods .+ loaded on (NeoForge|Fabric|Forge)'
                )) {
                    $clientOk = $true
                    $reason = 'client reached main menu / recipes loaded'
                    Start-Sleep -Seconds 3
                    break
                }
            }
            Start-Sleep -Seconds 5
        }

        Stop-ClientTree $proc
        Start-Sleep -Seconds 2

        $status = if ($clientOk) { 'PASS' } else { 'FAIL' }
        $color = if ($clientOk) { 'Green' } else { 'Red' }
        Write-Log "CLIENT $status ($reason)" $color
        $results.Add([pscustomobject]@{ Target = $label; Build = 'OK'; Client = $status; Note = $reason })
    }
}

Write-Log "`n========== SUMMARY ==========" 'Cyan'
$results | Format-Table -AutoSize | Out-String | ForEach-Object {
    Write-Host $_.TrimEnd()
    Add-Content -Path $logPath -Value $_.TrimEnd() -Encoding utf8
}

$failed = @($results | Where-Object { $_.Build -ne 'OK' -or $_.Client -ne 'PASS' })
Write-Log "Smoke finished $(Get-Date -Format o) - $($results.Count - $failed.Count)/$($results.Count) passed"
if ($failed.Count -gt 0) { exit 1 }
exit 0
