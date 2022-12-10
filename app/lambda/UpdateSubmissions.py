import boto3
import json

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('Forms')


def lambda_handler(event, context):

    # Get Event Type
    event_type = event['Records'][0]['eventName']

    # Get User ID
    user_id = event['Records'][0]['dynamodb']['NewImage']['user_id']['S']
    # Get Form ID
    form_id = event['Records'][0]['dynamodb']['Keys']['form_id']['S']

    if event_type == 'INSERT':
        data = table.get_item(
            Key={
                'user_id': user_id,
                'form_id': form_id
            }
        )

        data['Item']['submissions_count'] += 1

        table.put_item(Item=data['Item'])

    return {
        'statusCode': 200,
        'body': json.dumps('A new submission was added to the table')
    }
