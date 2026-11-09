param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $MavenArguments = @('clean', 'compile', 'javafx:run')
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$mavenHome = Join-Path $projectRoot '.tools\apache-maven-3.9.10'
$maven = Join-Path $mavenHome 'bin\mvn.cmd'

if (-not (Test-Path -LiteralPath $maven)) {
    throw "Không tìm thấy Maven cục bộ tại $maven. Hãy cài Maven theo README.md."
}

$env:MAVEN_HOME = $mavenHome
$localRepository = Join-Path $projectRoot '.m2\repository'
New-Item -ItemType Directory -Force -Path $localRepository | Out-Null
$repoArgument = "-Dmaven.repo.local=$localRepository"
& $maven $repoArgument '-f' (Join-Path $projectRoot 'pom.xml') @MavenArguments
exit $LASTEXITCODE
