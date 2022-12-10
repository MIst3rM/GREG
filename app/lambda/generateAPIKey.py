import json
import boto3

client = boto3.client('apigateway')


def lambda_handler(event, context):

    response = client.create_api_key(
        name=event['name'],
        enabled=True,
        stageKeys=[
            {
                'restApiId': 'olwzb7pjjb',
                'stageName': 'test'
            },
        ]
    )

    api_key_id = response['id']

    usageresponse = client.create_usage_plan_key(
        usagePlanId='3z0wfj',
        keyId=api_key_id,
        keyType='API_KEY'
    )

    return {
        'statusCode': 200,
        'body': json.dumps(response['value'])
    }
