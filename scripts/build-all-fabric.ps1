$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$versions = @('1.16.5', '1.18.2', '1.19.2', '1.20.1', '1.21.1', '1.21.8', '26.1')
Push-Location $root
foreach ($v in $versions) {
    Write-Host "=== Building fabric-$v ===" -ForegroundColor Cyan
    & .\gradlew ":fabric-${v}:build" --no-daemon "-PtogLoader=fabric" "-PfabricVersions=$v"
    if ($LASTEXITCODE -ne 0) { throw "fabric-$v build failed" }
}
Pop-Location
Write-Host "All Fabric builds succeeded." -ForegroundColor Green
