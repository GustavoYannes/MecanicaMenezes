@echo off
setlocal EnableExtensions EnableDelayedExpansion
title Mecanica Menezes - Parar Sistema

echo ====================================
echo MECANICA MENEZES
echo Parar sistema
echo ====================================
echo.
echo Este script procura processos escutando nas portas 4200 e 8080.
echo.

call :kill_port 4200 "front-end estatico Python"
call :kill_port 8080 "back-end Spring Boot"

echo.
echo Finalizado.
pause
exit /b 0

:kill_port
set "PORT=%~1"
set "SERVICE_NAME=%~2"
set "FOUND="
set "KILLED_PIDS= "

for /f "tokens=5" %%P in ('netstat -ano ^| findstr /R /C:":%PORT% .*LISTENING"') do (
  set "FOUND=1"
  if "!KILLED_PIDS: %%P =!"=="!KILLED_PIDS!" (
    set "KILLED_PIDS=!KILLED_PIDS!%%P "
    echo Encerrando %SERVICE_NAME% na porta %PORT% ^(PID %%P^)...
    taskkill /PID %%P /F >nul 2>nul
    if errorlevel 1 (
      echo Nao foi possivel encerrar o PID %%P. Feche a janela manualmente.
    ) else (
      echo Processo encerrado.
    )
  )
)

if not defined FOUND (
  echo Nenhum processo encontrado na porta %PORT% ^(%SERVICE_NAME%^).
)
exit /b 0
