# Iniciar o Sistema

## Como iniciar

1. Dê dois cliques em `scripts\iniciar-sistema.bat`.
2. Espere abrir uma janela para o back-end e outra para o front-end.
3. Use o endereço mostrado na janela principal.

No computador:

```text
http://localhost:4200
```

No celular, use o IP mostrado pelo script, por exemplo:

```text
http://192.168.0.10:4200
```

## Como parar

Feche as janelas do back-end e do front-end, ou dê dois cliques em:

```text
scripts\parar-sistema.bat
```

## Observações

O front-end roda em `0.0.0.0:4200` com `npm run dev:network`.

O back-end roda em `0.0.0.0:8080` usando `backoficina\mvnw.cmd` quando disponível. O script também configura `CORS_ALLOWED_ORIGINS` com `localhost` e com o IP detectado.

No modo network, o front-end calcula a API automaticamente pelo host usado no navegador. Assim, ao acessar `http://IP_DO_PC:4200` pelo celular, a API será chamada em `http://IP_DO_PC:8080/api`, sem editar IP manualmente.
