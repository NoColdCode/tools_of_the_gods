# Compile key loader targets; log output to build/compile-logs to avoid terminal overload.
param(
    [string[]]$Targets = @(
        'forge-1.18.2',
        'forge-1.19.2',
        'forge-1.20.1',
        'neoforge-1.21.1',
        'neoforge-1.21.8',
        'neoforge-26.1',
        'fabric-1.21.1'
    )
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$logDir = Join-Path $root 'build\compile-logs'
New-Item -ItemType Directory -Force -Path $logDir | Out-Null
Push-Location $root

$taskMap = @{
    'forge-1.18.2'    = @('-PtogLoader=forge', '-PforgeVersions=1.18.2', ':forge-1.18.2:compileJava')
    'forge-1.19.2'    = @('-PtogLoader=forge', '-PforgeVersions=1.19.2', ':forge-1.19.2:compileJava')
    'forge-1.20.1'    = @('-PtogLoader=forge', '-PforgeVersions=1.20.1', ':forge-1.20.1:compileJava')
    'neoforge-1.21.1' = @('-PtogLoader=neoforge', '-PneoforgeVersions=1.21.1', ':neoforge-1.21.1:compileJava')
    'neoforge-1.21.8' = @('-PtogLoader=neoforge', '-PneoforgeVersions=1.21.8', ':neoforge-1.21.8:compileJava')
    'neoforge-26.1'   = @('-PtogLoader=neoforge', '-PneoforgeVersions=26.1', ':neoforge-26.1:compileJava')
    'fabric-1.21.1'   = @('-PtogLoader=fabric', '-PfabricVersions=1.21.1', ':fabric-1.21.1:compileJava')
}

$results = @()
foreach ($name in $Targets) {
    if (-not $taskMap.ContainsKey($name)) {
        throw "Unknown target '$name'"
    }
    $logFile = Join-Path $logDir "$name.log"
    Write-Host "Building $name -> $logFile"
    $gradleArgs = @('--no-daemon', '--console=plain', '--quiet') + $taskMap[$name]
    $prevEap = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    & .\gradlew @gradleArgs 2>&1 | Out-File -FilePath $logFile -Encoding utf8
    $code = $LASTEXITCODE
    $ErrorActionPreference = $prevEap
    $status = if ($code -eq 0) { 'OK' } else { "FAIL($code)" }
    Write-Host "  $status"
    $results += [pscustomobject]@{ Target = $name; Status = $status; Log = $logFile }
}

Pop-Location
Write-Host "`n=== COMPILE SUMMARY ==="
$results | Format-Table -AutoSize
if ($results | Where-Object { $_.Status -ne 'OK' }) { exit 1 }
