@echo off
setlocal EnableExtensions
title Mecanica Menezes - Iniciar Sistema

cd /d "%~dp0\.."

echo ====================================
echo MECANICA MENEZES
echo Sistema iniciando...
echo ====================================
echo.

if not exist "frontOficina\package.json" (
  echo ERRO: Este script deve ser executado a partir do projeto Mecanica Menezes.
  echo Pasta esperada nao encontrada: frontOficina\package.json
  echo.
  pause
  exit /b 1
)

if not exist "backoficina\pom.xml" (
  echo ERRO: Este script deve ser executado a partir do projeto Mecanica Menezes.
  echo Pasta esperada nao encontrada: backoficina\pom.xml
  echo.
  pause
  exit /b 1
)

echo Verificando dependencias...

where node >nul 2>nul
if errorlevel 1 (
  echo ERRO: Node.js nao encontrado.
  echo Instale o Node.js e tente novamente.
  echo.
  pause
  exit /b 1
)

where npm >nul 2>nul
if errorlevel 1 (
  echo ERRO: NPM nao encontrado.
  echo Instale o Node.js com NPM e tente novamente.
  echo.
  pause
  exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
  echo ERRO: Java nao encontrado.
  echo Instale o Java/JDK e tente novamente.
  echo.
  pause
  exit /b 1
)

if not exist "frontOficina\node_modules" (
  echo ERRO: Dependencias do front-end nao instaladas.
  echo Execute este comando uma vez:
  echo   cd frontOficina
  echo   npm install
  echo.
  pause
  exit /b 1
)

if not exist "backoficina\.env" (
  echo AVISO: backoficina\.env nao encontrado.
  echo O back-end pode falhar se DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET e CORS_ALLOWED_ORIGINS nao estiverem configurados.
  echo Voce pode copiar backoficina\.env.example para backoficina\.env e ajustar os valores.
  echo.
)

for /f "usebackq delims=" %%I in (`powershell -NoProfile -ExecutionPolicy Bypass -Command "$ip = Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.IPAddress -notlike '127.*' -and $_.IPAddress -notlike '169.254.*' -and $_.IPAddress -notlike '0.*' -and $_.AddressState -eq 'Preferred' } | Sort-Object InterfaceIndex | Select-Object -First 1 -ExpandProperty IPAddress; if (-not $ip) { $ip = 'localhost' }; Write-Output $ip"`) do set "LOCAL_IP=%%I"

if not defined LOCAL_IP set "LOCAL_IP=localhost"

echo.
echo ====================================
echo IP detectado: %LOCAL_IP%
echo.
echo Acesse no computador: http://localhost:4200
echo Acesse no celular:     http://%LOCAL_IP%:4200
echo API:                  http://%LOCAL_IP%:8080/api
echo ====================================
echo.

call :check_port 4200 "front-end Angular"
if errorlevel 1 exit /b 1

call :check_port 8080 "back-end Spring Boot"
if errorlevel 1 exit /b 1

set "BACK_COMMAND=call mvn spring-boot:run"
if exist "backoficina\mvnw.cmd" set "BACK_COMMAND=call mvnw.cmd spring-boot:run"

echo Abrindo janela do back-end...
start "Mecanica Menezes - Back-end" cmd /k "cd /d ""%CD%\backoficina"" && set SERVER_ADDRESS=0.0.0.0&& set SERVER_PORT=8080&& set CORS_ALLOWED_ORIGINS=http://localhost:4200,http://%LOCAL_IP%:4200&& %BACK_COMMAND%"

echo Abrindo janela do front-end...
start "Mecanica Menezes - Front-end" cmd /k "cd /d ""%CD%\frontOficina"" && npm run dev:network"

echo.
echo Sistema solicitado para iniciar.
echo Aguarde as duas janelas terminarem de carregar.
echo.
echo Acesse no computador: http://localhost:4200
echo Acesse no celular:     http://%LOCAL_IP%:4200
echo API:                  http://%LOCAL_IP%:8080/api
echo.
echo Esta janela pode ficar aberta para consulta das URLs.
pause
exit /b 0

:check_port
set "PORT=%~1"
set "SERVICE_NAME=%~2"
netstat -ano | findstr /R /C:":%PORT% .*LISTENING" >nul 2>nul
if not errorlevel 1 (
  echo AVISO: A porta %PORT% usada pelo %SERVICE_NAME% ja esta em uso.
  choice /C SN /N /M "Deseja continuar mesmo assim? (S/N): "
  if errorlevel 2 (
    echo Operacao cancelada pelo usuario.
    echo.
    pause
    exit /b 1
  )
)
exit /b 0
