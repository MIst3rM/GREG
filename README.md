# GREG

## ⚠️ On Windows Only Section Start ⚠️
Docker desktop with hyper-v backend engine will require you to share project files with docker before you can start the project.

## Sharing Project Files
1. Open Docker Desktop
2. Go in settings
3. Click on resources > File sharing
4. Add the app directory of the project
5. Click on apply & restart
6. Yon can now start the project

## ⚠️ On Windows Only Section End ⚠️
</br>

## Building the project

```bash
docker compose build
```

## Starting the project for the first time

### Install node modules
```bash
docker compose run --rm frontend yarn
```

### Inject Environment Variables
1.  Create a .env file inside the app folder
2.  Set the following variables:
    1.  AWS_ACCESS_KEY_ID=\<Your Key ID\>
    2.  AWS_SECRET_ACCESS_KEY=\<Your Secret Key\>
    3.  AWS_REGION=\<Preffered Aws Region\>
    4.  DYNAMO_ENDPOINT=\<DB Endpoint Url\>

### Bring Up Containers
```bash
docker compose up -d
```



