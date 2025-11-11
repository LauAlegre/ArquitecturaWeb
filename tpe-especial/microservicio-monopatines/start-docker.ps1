param(
  [switch]$Rebuild
)

Write-Host "Comprobando Docker..."
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
  Write-Error "Docker no está instalado o no está en PATH."
  exit 1
}

try {
  docker info > $null 2>&1
} catch {
  Write-Error "Docker no está corriendo. Abre Docker Desktop y vuelve a intentarlo."
  exit 1
}

# ir a la carpeta del script (asume que el docker-compose.yml está en la misma carpeta)
Set-Location -Path (Split-Path -Path $MyInvocation.MyCommand.Path -Parent)

if ($Rebuild) {
  Write-Host "Reconstruyendo imágenes (opción --Rebuild usada)..."
  docker compose build --no-cache
}

Write-Host "Levantando contenedores en segundo plano..."
docker compose up -d

Write-Host "`nEstado de los servicios:"
docker compose ps

Write-Host "`nLogs (últimas 50 líneas) de monopatines-db:"
docker compose logs --tail=50 monopatines-db

Write-Host "`nComandos útiles:"
Write-Host "  Ver logs en tiempo real: docker compose logs -f"
Write-Host "  Entrar a la BD (psql dentro del contenedor):"
Write-Host "    docker exec -it monopatines-db psql -U monopatines_user -d monopatines"
Write-Host "  Parar y remover: docker compose down --volumes"
Write-Host "`nPuertos expuestos en el host: DB -> 5434, Servicio -> 8081"
