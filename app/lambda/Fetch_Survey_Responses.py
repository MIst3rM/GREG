import json
import boto3
from boto3.dynamodb.conditions import Key, Attr

dynamodb = boto3.resource('dynamodb')
table1 = dynamodb.Table('Forms')
table2 = dynamodb.Table('Form_Submissions')


def lambda_handler(event, context):

    response = {}

    # Make sure survey with provided form_id is publicly shared
    data = table1.scan(
        ProjectionExpression='form_id',
        FilterExpression=Attr('sharedPublicly').eq(True)
    )

    if {'form_id': event["queryStringParameters"]["form_id"]} in data['Items']:

        data = table2.query(
            ProjectionExpression='answers',
            KeyConditionExpression=Key('form_id').eq(
                event["queryStringParameters"]["form_id"])
        )

        grouped_answers = {}
        for answer in data["Items"]:
            for question, answer in answer['answers'].items():
                if question not in grouped_answers:
                    grouped_answers[question] = []
                grouped_answers[question].append(answer)

        response = {
            'statusCode': 200,
            'body': json.dumps(grouped_answers),
            'headers': {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
        }

    else:
        response = {
            'statusCode': 400,
            'body': 'Something went wrong',
            'headers': {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
        }

    return response
