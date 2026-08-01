# Regenerate .vscode/launch.json with a Client entry for every loader version.
# Run after changing gradle.properties version lists or adding a new subproject.
param(
    [switch]$PrepareRuns
)

$root = Split-Path -Parent $PSScriptRoot
Push-Location $root

$fabricVersions   = @('1.16.5', '1.18.2', '1.19.2', '1.20.1', '1.21.1', '1.21.8', '26.1')
$neoforgeVersions = @('1.21.1', '1.21.8', '26.1')
$forgeVersions    = @('1.18.2', '1.19.2', '1.20.1')

$ws = '${workspaceFolder}'

function New-ModDevClientConfig {
    param(
        [string]$Loader,
        [string]$Version,
        [bool]$IsForge = $false
    )
    $project = "${Loader}-${Version}"
    $dir = if ($Loader -eq 'neoforge') { "neoforge/$Version" } else { "forge/$Version" }
    $binMain = "$ws/$dir/bin/main".Replace('/', '\')
    $cfg = @{
        type       = 'java'
        request    = 'launch'
        name       = "$project - Client"
        presentation = @{
            group = "Mod Development - $project"
            order = 0
        }
        projectName = $project
        mainClass   = 'net.neoforged.devlaunch.Main'
        args        = @("@$ws/$dir/build/moddev/clientRunProgramArgs.txt".Replace('/', '\'))
        vmArgs      = @(
            "@$ws/$dir/build/moddev/clientRunVmArgs.txt".Replace('/', '\'),
            "-Dfml.modFolders=tools_of_the_gods%$binMain"
        )
        cwd         = "$ws/$dir/run".Replace('/', '\')
        env         = if ($IsForge) { @{ MOD_CLASSES = "tools_of_the_gods%$binMain" } } else { @{} }
        console     = 'internalConsole'
        shortenCommandLine = 'none'
    }
    return $cfg
}

function New-FabricClientConfig {
    param([string]$Version)
    $project = "fabric-$Version"
    $dir = "fabric/$Version"
    $loomCfg = "$ws/$dir/.gradle/loom-cache/launch.cfg".Replace('/', '\')
    return @{
        type        = 'java'
        request     = 'launch'
        name        = "$project - Client"
        presentation = @{
            group = "Mod Development - $project"
            order = 0
        }
        projectName = $project
        mainClass   = 'net.fabricmc.devlaunchinjector.Main'
        vmArgs      = "-Dfabric.dli.config=$loomCfg -Dfabric.dli.env=client -Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient"
        args        = ''
        cwd         = "$ws/$dir/run".Replace('/', '\')
        env         = @{}
        console     = 'integratedTerminal'
        stopOnEntry = $false
    }
}

if ($PrepareRuns) {
    Write-Host "Preparing moddev client runs..." -ForegroundColor Cyan
    foreach ($v in $neoforgeVersions) {
        & .\gradlew.bat ":neoforge-${v}:prepareClientRun" "-PtogLoader=neoforge" "-PneoforgeVersions=$v" --no-daemon 2>&1 | Out-Null
    }
    foreach ($v in $forgeVersions) {
        & .\gradlew.bat ":forge-${v}:prepareClientRun" "-PtogLoader=forge" "-PforgeVersions=$v" --no-daemon 2>&1 | Out-Null
    }
    foreach ($v in $fabricVersions) {
        & .\gradlew.bat ":fabric-${v}:configureClientLaunch" "-PtogLoader=fabric" "-PfabricVersions=$v" --no-daemon 2>&1 | Out-Null
    }
}

$configs = @()
foreach ($v in $fabricVersions) {
    $configs += New-FabricClientConfig -Version $v
}
foreach ($v in $neoforgeVersions) {
    $configs += New-ModDevClientConfig -Loader 'neoforge' -Version $v -IsForge $false
}
foreach ($v in $forgeVersions) {
    $configs += New-ModDevClientConfig -Loader 'forge' -Version $v -IsForge $true
}

$launch = @{
    version = '0.2.0'
    configurations = $configs
}

$outPath = Join-Path $root '.vscode\launch.json'
$json = $launch | ConvertTo-Json -Depth 10
# ConvertTo-Json escapes poorly for launch.json; fix common issues
$json = $json -replace '\\u003d', '='
$json = $json -replace '\\u0026', '&'
[System.IO.File]::WriteAllText($outPath, $json)

Write-Host "Wrote $($configs.Count) client launch configs to $outPath" -ForegroundColor Green
Write-Host "Tip: run with -PrepareRuns first if moddev arg files are missing." -ForegroundColor DarkGray
Pop-Location
