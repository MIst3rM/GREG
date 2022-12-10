import json
import boto3
from datetime import datetime, timezone
import pytz

timezone = pytz.timezone("America/Toronto")

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('Forms')


def lambda_handler(event, context):
    data = table.scan()
    data = data['Items']
    for item in data:
        if item['status'] == 'ACTIVE':
            closing_at = datetime.strptime(
                item['closing_at'], '%Y-%m-%dT%H:%M:%S')
            closing_at = timezone.localize(closing_at)

            if datetime.now().timestamp() > closing_at.timestamp():
                item['status'] = 'EXPIRED'
                table.put_item(Item=item)
    print(data)
    return {
        'statusCode': 200,
        'body': json.dumps('Survey Statuses Updated')
    }
