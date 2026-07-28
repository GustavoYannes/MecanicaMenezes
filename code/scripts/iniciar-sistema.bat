@echo off
setlocal EnableExtensions EnableDelayedExpansion
title Mecanica Menezes - Iniciar Sistema

cd /d "%~dp0\.."

echo ====================================
echo MECANICA MENEZES
echo Sistema iniciando...
echo ====================================
echo.

if not exist "frontOficina\dist\frontOficina\browser\index.html" (
  echo ERRO: Build estatico do front-end nao encontrado.
  echo Pasta esperada: frontOficina\dist\frontOficina\browser
  echo.
  echo Gere o build em uma maquina com Node moderno antes de levar para a oficina:
  echo   cd frontOficina
  echo   npm run build:prod
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

where java >nul 2>nul
if errorlevel 1 (
  echo ERRO: Java nao encontrado.
  echo Instale o Java/JDK e tente novamente.
  echo.
  pause
  exit /b 1
)

set "PYTHON_CMD="
py -3 --version >nul 2>nul
if not errorlevel 1 set "PYTHON_CMD=py -3 scripts\spa-server.py dist\frontOficina\browser 4200"

if not defined PYTHON_CMD (
  where python >nul 2>nul
  if errorlevel 1 (
    echo ERRO: Python nao encontrado.
    echo Instale Python 3 para servir o front-end Angular.
    echo.
    pause
    exit /b 1
  )

  for /f "tokens=2 delims= " %%V in ('python --version 2^>^&1') do set "PYTHON_VERSION=%%V"
  echo !PYTHON_VERSION! | findstr /R "^3\." >nul 2>nul
  if not errorlevel 1 (
    set "PYTHON_CMD=python scripts\spa-server.py dist\frontOficina\browser 4200"
  ) else (
    echo ERRO: Python 3 nao encontrado.
    echo O servidor padrao do Python 2 nao suporta as rotas do Angular, como /login e /dashboard.
    echo Instale Python 3 ou use IIS com o web.config gerado no build.
    echo.
    pause
    exit /b 1
  )
)
if not exist "backoficina\.env" (
  echo AVISO: backoficina\.env nao encontrado.
  echo O back-end pode falhar se DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET e CORS_ALLOWED_ORIGINS nao estiverem configurados.
  echo Voce pode copiar backoficina\.env.example para backoficina\.env e ajustar os valores.
  echo.
)

set "LOCAL_IP=localhost"
for /f "tokens=2 delims=:" %%I in ('ipconfig ^| findstr /I "IPv4"') do (
  set "LOCAL_IP=%%I"
  goto :ip_found
)
:ip_found
for /f "tokens=* delims= " %%A in ("%LOCAL_IP%") do set "LOCAL_IP=%%A"
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

call :check_port 4200 "front-end estatico"
if errorlevel 1 exit /b 1

call :check_port 8080 "back-end Spring Boot"
if errorlevel 1 exit /b 1

set "BACK_COMMAND=mvn spring-boot:run"
if exist "backoficina\mvnw.cmd" set "BACK_COMMAND=mvnw.cmd spring-boot:run"

echo Abrindo janela do back-end...
start "Mecanica Menezes - Back-end" cmd /k "cd /d ""%CD%\backoficina"" && set SERVER_ADDRESS=0.0.0.0&& set SERVER_PORT=8080&& set CORS_ALLOWED_ORIGINS=http://localhost:4200,http://%LOCAL_IP%:4200&& %BACK_COMMAND%"

echo Abrindo janela do front-end estatico...
start "Mecanica Menezes - Front-end" cmd /k "cd /d ""%CD%\frontOficina"" && %PYTHON_CMD%"

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



