# GREG

A web application for creating, sharing and collecting responses to surveys.

- **Frontend**: Vue 3, TypeScript, Vite, Pinia and Element Plus, with sign-in through Amazon Cognito
- **Backend**: Spring Boot 2.7 (Java 17) with DynamoDB
- **Serverless**: AWS Lambda functions for API keys, survey status and response aggregation (`app/lambda/`)
- **Local setup**: Docker Compose (nginx, frontend and backend containers)

## Project Structure

```
app/
  docker-compose.yml    nginx + frontend + backend
  .env.example          Environment variables used by the containers
  src/frontend/         Vue 3 + Vite app
  src/backend/          Spring Boot API
  lambda/               AWS Lambda functions (Python)
```

## Getting Started

> **Windows only:** Docker Desktop with the Hyper-V backend needs access to the project files first.
> Open Docker Desktop → **Settings** → **Resources** → **File sharing**, add the `app` folder, then click **Apply & restart**.

All commands below are run from the `app` folder.

### 1. Set the environment variables

```bash
cp .env.example .env
```

Fill it in (Docker Compose passes it to both containers). To run the backend outside Docker, do the same with `src/backend/.env.example`.

| Variable | Used by | Description |
| --- | --- | --- |
| `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY` | backend | AWS credentials with DynamoDB access |
| `AWS_REGION` | backend | AWS region, e.g. `us-east-1` |
| `DYNAMO_ENDPOINT` | backend | DynamoDB endpoint URL |
| `VITE_COGNITO_CLIENT_ID` | frontend | Cognito app client ID |
| `VITE_COGNITO_APP_DOMAIN` | frontend | Cognito hosted UI domain |
| `VITE_COGNITO_REDIRECT_URI` | frontend | Where Cognito redirects after sign-in |
| `VITE_COGNITO_USERPOOL_ID` | frontend | Cognito user pool ID |
| `VITE_APP_URL` | frontend | Public URL of the app |

### 2. Build the containers

```bash
docker compose build
```

### 3. Install the frontend dependencies

```bash
docker compose run --rm frontend yarn
```

### 4. Start everything

```bash
docker compose up -d
```

The app is served through nginx on <http://localhost>.

## AWS Lambda Functions

The functions in `app/lambda/` run on AWS (Python runtime with `boto3`):

| Function | Trigger | What it does |
| --- | --- | --- |
| `generateAPIKey.py` | API Gateway | Creates an API key and adds it to the API's usage plan |
| `Fetch_Survey_Responses.py` | API Gateway | Returns the answers of a publicly shared survey, grouped by question |
| `UpdateSubmissions.py` | DynamoDB stream (new submissions) | Increments a survey's `submissions_count` in the `Forms` table when a response is added |
| `UpdateSurveyStatus.py` | Schedule | Marks active surveys as `EXPIRED` once their closing time has passed |

`UpdateSurveyStatus.py` also needs `pytz`, which must be packaged with the function or added as a layer.

## License

[GNU AGPL v3](LICENSE)
