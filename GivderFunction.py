import json
import random
import boto3
import secrets
import string
import datetime


def lambda_handler(event, context):
    # TODO implement
    if event['action'] == "SignIn":
        return signIn(event['phoneNumber'])
    elif event['action'] == "MatchCode":
        #return event
        return matchCode(event['phoneNumber'],event['code'])
    elif event['action'] == "RefreshToken":
        return refreshToken(event['refreshToken'])
    elif event['action'] == "Login":
        return logIn(event['phoneNumber'],event['password'])
    elif event['action'] == "GetPassword":
        return getPassword(event['phoneNumber'],event['code'])
    if event['action'] == "PushLog":
        return pushLog(event['log'])
    if event['action'] == "SendSMS":
        return json.dumps(sendSMS(event['phoneNumber'],event['text']))
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
    
    

def sendSMS(phoneNumber,msg):
    sns = boto3.client('sns')
    number = phoneNumber
    sns.publish(PhoneNumber = number, Message=msg, MessageAttributes={'AWS.SNS.SMS.SenderID': {'DataType': 'String', 'StringValue': "SmsBot"}, 'AWS.SNS.SMS.SMSType': {'DataType': 'String', 'StringValue': 'Promotional'}})
    return "sent"
def pushLog(log):
    dynamodb = boto3.client('dynamodb')
    time = datetime.datetime.now()
    dynamodb.put_item(TableName='TrigLogs',Item={'Log': {'S': str(log)}, 'Time': {'S': str(time)}})
    return "logged"
    
def refreshToken(token):
    cognito = boto3.client('cognito-idp')
    resp = cognito.admin_initiate_auth( UserPoolId=USER_POOL_ID,ClientId=CLIENT_ID,AuthFlow='REFRESH_TOKEN', AuthParameters={'REFRESH_TOKEN': token, })
   
    return resp['AuthenticationResult']
    
    
def getPassword(phoneNumber,code):
    dynamodb = boto3.client('dynamodb')
    cognito = boto3.client('cognito-idp')
    
    response = dynamodb.get_item(Key={'PhoneNumber': {'S': phoneNumber}},  TableName='GivderOtp')
    
    if response['Item']['Code']['S'] == code:
        response = dynamodb.get_item(Key={'PhoneNumber': {'S': phoneNumber}},  TableName='GivderPasswords')['Item']
        password = response['Password']['S']
        return password
    else:
        return ""
def logIn(phoneNumber,password):
    dynamodb = boto3.client('dynamodb')
    cognito = boto3.client('cognito-idp')

    
    
    try:
        resp = cognito.admin_initiate_auth( UserPoolId=USER_POOL_ID,ClientId=CLIENT_ID,AuthFlow='ADMIN_NO_SRP_AUTH',
                 AuthParameters={
                     'USERNAME': phoneNumber,
                     'PASSWORD': password,
                  },
                ClientMetadata={
                  'username': phoneNumber,
                  'password': password,
              })
        return resp['AuthenticationResult']
    except Exception as ex:
        print(ex)
        return ""
    
def matchCode(phoneNumber,code):
    dynamodb = boto3.client('dynamodb')
    cognito = boto3.client('cognito-idp')
    
    alphabet = string.ascii_letters + string.digits
    password = ''.join(secrets.choice(alphabet) for i in range(20)) 
    
    
    response = dynamodb.get_item(Key={'PhoneNumber': {'S': phoneNumber}},  TableName='GivderOtp')
    
    if response['Item']['Code']['S'] == code:
        try:
            cognito.sign_up(ClientId=CLIENT_ID,Username=phoneNumber, Password=password)
            cognito.admin_confirm_sign_up( UserPoolId=USER_POOL_ID, Username=phoneNumber)
            resp = cognito.admin_initiate_auth( UserPoolId=USER_POOL_ID,ClientId=CLIENT_ID,AuthFlow='ADMIN_NO_SRP_AUTH',
                 AuthParameters={
                     'USERNAME': phoneNumber,
                     'PASSWORD': password,
                  },
                ClientMetadata={
                  'username': phoneNumber,
                  'password': password,
              })
            dynamodb.put_item(TableName='GivderPasswords',Item={'PhoneNumber': {'S': str(phoneNumber)}, 'Password': {'S': str(password)}})
            return resp['AuthenticationResult']
    
    
        except:
            response = dynamodb.get_item(Key={'PhoneNumber': {'S': phoneNumber}},  TableName='GivderPasswords')['Item']
            print(response)
            password = response['Password']['S']
            resp = cognito.admin_initiate_auth( UserPoolId=USER_POOL_ID,ClientId=CLIENT_ID,AuthFlow='ADMIN_NO_SRP_AUTH',
                 AuthParameters={
                     'USERNAME': phoneNumber,
                     'PASSWORD': password,
                  },
                ClientMetadata={
                  'username': phoneNumber,
                  'password': password,
              })
            return resp['AuthenticationResult']
    else:
        return ""
def signIn(phoneNumber):
    
    dynamodb = boto3.client('dynamodb')
    
    #opt = random.randint(1000,9999)
    opt = 4458
    sns = boto3.client('sns')
    number = phoneNumber
    sns.publish(PhoneNumber = number, Message='Your Givder verification code is '+str(opt) )
    
    dynamodb.put_item(TableName='GivderOtp', Item={'PhoneNumber': {'S': str(phoneNumber)},  'Code': {'S': str(opt)} })
    return "success"
