Perfecto, Migue 🙌. Te dejo un `README.md` listo para pegar en tu repo. Lo armé pensando en que sea claro para cualquier desarrollador que quiera correr y probar tu MVP.

---

## 🔹 `README.md`

````markdown
# 📊 Liferay Log Analyzer

Microservicio en **Spring Boot** que analiza archivos de log (`.log`) de **Liferay** y devuelve un reporte con el total de errores, tipos de excepciones y fallos detectados.

## 🚀 Características

- Procesa archivos `.log` cargados vía `multipart/form-data`.
- Detecta:
  - Excepciones (`NullPointerException`, `PortalException`, etc.).
  - Fallos de conexión y tiempo (`Timeout`, `Timed out`).
  - Operaciones fallidas (`failed`, `unable to`).
  - Errores genéricos (`ERROR`).
- Permite filtrar por **rango de fecha y hora**.
- Respuesta en **JSON** con totales y detalle.

---

## 🛠️ Requisitos

- Java 17+
- Maven 3.8+
- Spring Boot 3.x

---

## ▶️ Cómo ejecutar

Clona el proyecto y compílalo:

```bash
git clone https://github.com/tuusuario/liferay-log-analyzer.git
cd liferay-log-analyzer
mvn spring-boot:run
````

La aplicación corre por defecto en:

```
http://localhost:8080
```

---

## 📡 Endpoint principal

### Analizar archivo de logs

```
POST /logs/analyze
```

### Parámetros

* **file** (obligatorio) → archivo `.log` (tipo *File* en Postman o curl).
* **startDateTime** (opcional) → fecha inicial, formato `yyyy-MM-dd HH:mm:ss`.
* **endDateTime** (opcional) → fecha final, formato `yyyy-MM-dd HH:mm:ss`.

---

## 🧪 Ejemplo con Postman

1. Método: **POST**
2. URL:

   ```
   http://localhost:8080/logs/analyze
   ```
3. Body → `form-data`:

| Key           | Value                  | Type |
| ------------- | ---------------------- | ---- |
| file          | liferay.2025-08-26.log | File |
| startDateTime | 2025-08-26 10:00:00    | Text |
| endDateTime   | 2025-08-26 12:00:00    | Text |

---

## 🧪 Ejemplo con curl

```bash
curl -X POST http://localhost:8080/logs/analyze \
  -F "file=@/ruta/liferay.2025-08-26.log" \
  -F "startDateTime=2025-08-26 10:00:00" \
  -F "endDateTime=2025-08-26 12:00:00"
```

---

## 📄 Ejemplo de respuesta

```json
{
  "totalErrors": 7,
  "details": [
    { "errorType": "NullPointerException", "count": 3 },
    { "errorType": "Timeout", "count": 2 },
    { "errorType": "FailedOperation", "count": 1 },
    { "errorType": "GenericError", "count": 1 }
  ]
}
```

---

## 🔮 Futuras mejoras

* Persistencia en base de datos (PostgreSQL / MongoDB).
* Métricas expuestas vía Spring Boot Actuator.
* Dashboard con gráficos (React + Chart.js/Recharts).
* Integración con sistemas de monitoreo (Prometheus / Grafana).
* Detección de más patrones específicos de Liferay (`ORA-`, `SocketTimeoutException`, etc.).

---

👨‍💻 Autor: **Miguel Fernandez Romero**


