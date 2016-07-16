# -*- coding: utf-8 -*-
import boto.sqs
import json
import pymysql
import threading

POLLING_INTERVAL = 5.0

def logQueue():
    threading.Timer(POLLING_INTERVAL, logQueue).start()
    f = open("databaseDetails.txt")
    hostname = f.readline()
    username = f.readline()
    password = f.readline()
    database = f.readline()
    queuename = f.readline()
    conn = pymysql.connect(host=hostname, unix_socket='/tmp/mysql.sock', user=username , passwd=password, db=database)
    cur = conn.cursor()
    conn2 = boto.sqs.connect_to_region("us-east-1")
    queue = conn2.get_queue(queuename)
    while 1 :
        rs = queue.get_messages()
        if len(rs) > 0 :
            message = rs[0].get_body()
            instance = message
            instance = json.loads(instance)
            queryParams =  [instance["Parameters"].encode('utf-8'),instance["IpAddress"], instance["Time to respond"], instance["Timestamp"],instance["Url"], instance["Response Code"]]
            queryParams = (', '.join('\'' + item + '\'' for item in queryParams))
            qry = "INSERT INTO Parameters2(`Parameters`,`IpAddress`,`Time to respond`,`Timestamp`,`Url`,`Response Code`) VALUES (" + queryParams + ");"
            cur.execute(qry)
            conn.commit()

logQueue()

