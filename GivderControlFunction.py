import json
import boto3
from botocore.errorfactory import ClientError



def lambda_handler(event, context):
    # TODO implement
    if event['action'] == 'PutUrl':
        return putFile(event['Key'])
    elif event['action'] == 'GetUrl':
        return getFile(event['Key'])
    elif event['action'] == 'AddUser':
        return addUser(event['phoneNumber'],event['userName'],event['contributionId'] ,event['type'],event['lat'],event['lon'],event['timeExpiration'] ,event['description'],event['plates'],event['color'],event['match'])
    elif event['action'] == 'GetContent':
        return getAllUnmatched()
    elif event['action'] == 'SendMessage':
        return sendMessage(event['to'],event['from'],event['message'],event['time'],event['type'])
    elif event['action'] == 'CheckMessages':
        return getMessages(event['to'])
    
    elif event['action'] == "NotifyNewId":
        return notifyNewId(event['phoneNumbers'],event['phoneNumber'])
    elif event['action'] == "AddDeviceId":
        return addDeviceId(event['phoneNumber'],event['deviceId'])
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
    
def sendMessage(to,fromP,message,time,type):
    dynamodb = boto3.client('dynamodb')
    dynamodb.put_item(TableName='GivderMessages', Item={
        'toMsg': {'S': str(to)}
    ,  'timeMsg': {'S': str(time)}
    ,  'messageMsg': {'S': str(message)}
    ,  'fromMsg': {'S': str(fromP)}
    ,  'typeMsg': {'S': str(type)}})
    
    return "done"
def getMessages(to):
    items = []
    dynamodb_client = boto3.client('dynamodb')
    response = dynamodb_client.query(TableName='GivderMessages',KeyConditionExpression='toMsg = :toMsg',ExpressionAttributeValues={':toMsg': {'S': to}})
    print(response)
    for item in response['Items']:
        body = {  'to': item['toMsg']['S'],  'time': item['timeMsg']['S'],  'message': item['messageMsg']['S']
            ,  'from': item['fromMsg']['S'],  'type': item['typeMsg']['S']}
        items.append(body)
    print(items)
    return items
def getAllUnmatched():
    dynamodb_client = boto3.client('dynamodb')
    cognitoclient = boto3.client('cognito-idp')

    more_pages = True
    pagination_token = None
    all_pages = []
    users=[]
    items = []
    while more_pages:
        params = {"UserPoolId":USER_POOL_ID,"ClientId":CLIENT_ID}
        if pagination_token is not None:
            params["PaginationToken"] = pagination_token
            response = cognitoclient.list_users(UserPoolId=USER_POOL_ID,PaginationToken= pagination_token)
        else:
            response = cognitoclient.list_users(UserPoolId=USER_POOL_ID)
        
        #pagination_token = response['PaginationToken']
        page = response['Users']
        for user in page:
            users.append(user['Attributes'][2]['Value'])
        more_pages = len(page) == 60


    for user in users:
        response = dynamodb_client.query(TableName='GivderUserDescription',KeyConditionExpression='PhoneNumber = :PhoneNumber',ExpressionAttributeValues={':PhoneNumber': {'S': user}})
        for item in response['Items']:
            body = {  'phoneNumber': item['PhoneNumber']['S'],  'contributionId': item['ContributionId']['S'],  'userName': item['UserName']['S']
            ,  'type': item['Type']['S'],  'lat': item['Lat']['S'],  'lon': item['Lon']['S'],  'timeExpiration': item['TimeExpiration']['S']
            ,  'description': item['Description']['S'],  'plates': item['Plates']['S'],  'color': item['Color']['S'],  'match': item['Match']['S']}
            items.append(body)
    print(users)
    print(items)
    return items
def addUser(phoneNumber, userName,contributionId, type,lat,lon,timeExpiration,description,plates,color,match):
    
    
    
    dynamodb = boto3.client('dynamodb')
    dynamodb.put_item(TableName='GivderUserDescription', Item={
        'PhoneNumber': {'S': str(phoneNumber)}
    ,  'ContributionId': {'S': str(contributionId)}
    ,  'UserName': {'S': str(userName)}
    ,  'Type': {'S': str(type)}
    ,  'Lat': {'S': str(lat)}
    ,  'Lon': {'S': str(lon)}
    ,  'TimeExpiration': {'S': str(timeExpiration)}
    ,  'Description': {'S': str(description)}
    ,  'Plates': {'S': str(plates)}
    ,  'Color': {'S': str(color)}
    ,  'Match': {'S': str(match)} })
    
    return "done"

def checkUsers(users):
    s3 = boto3.client('s3')
    doUsersExist = []
    for user in users:
        try:
            s3.head_object(Bucket=bucketName, Key=user)
            doUsersExist.append(True)
        except ClientError:
            # Not found
            doUsersExist.append(False)
    return doUsersExist
def putFile(video_id):
    s3 = boto3.client('s3', config=boto3.session.Config(signature_version='s3v4'))
    return s3.generate_presigned_url('put_object', Params={'Bucket':bucketName, 'Key':video_id}, ExpiresIn=21600, HttpMethod='PUT')
    
def getFile(key):
    s3 = boto3.client('s3', config=boto3.session.Config(signature_version='s3v4'))
    return s3.generate_presigned_url('get_object', Params={'Bucket':bucketName, 'Key':key}, ExpiresIn=21600)
    

def addDeviceId(phoneNumber,device_id):
    
    dynamodb = boto3.client('dynamodb')
    dynamodb.put_item(TableName='TringSubs',Item={'PhoneNumber': {'S': str(phoneNumber)}, 'DeviceId': {'S': str(device_id)}})
    
    return "success"
            
    
def getSubs(phoneNumbers):
    
    dynamodb = boto3.client('dynamodb')
    data = []
    for phoneNumber in phoneNumbers:
        response = dynamodb.get_item(Key={'PhoneNumber': {'S': phoneNumber}},  TableName='TringSubs')
        print(response)
        try:
            device = response['Item']['DeviceId']['S']
            data.append(device)
        except:
            ""
    return data
    
def notifyNewId(phoneNumbers,phoneNumber):
    sns = boto3.client('sns')
    body = {"PhoneNumber":phoneNumber}
    bodyJson = json.dumps(body)
    subs = getSubs(phoneNumbers)
    for sub in subs:
        send_push(sns,sub, bodyJson)
    return "success"
    
def send_push(sns,device_id, body):
    try:
        endpoint_response = sns.create_platform_endpoint( PlatformApplicationArn='arn:aws:sns:us-east-1:424961117306:app/GCM/Tring', Token=device_id,)   
        endpoint_arn = endpoint_response['EndpointArn']
    except Exception as err:
        print(err)
        result_re = re.compile(r'Endpoint(.*)already', re.IGNORECASE)
        result = result_re.search(err.message)
        if result:
            endpoint_arn = result.group(0).replace('Endpoint ','').replace(' already','')
        else:
            raise
            

    publish_result = sns.publish(
        TargetArn=endpoint_arn,
        Message=body,
    )
