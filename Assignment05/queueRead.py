# -*- coding: utf-8 -*-
import boto.sqs
import json
import pymysql
conn = pymysql.connect(host='aline-cnu-insights-dev.czuocyoc6awe.us-east-1.rds.amazonaws.com', unix_socket='/tmp/mysql.sock', user='abansal', passwd='abansal', db='cnu2016_abansal')
cur = conn.cursor()
conn2 = boto.sqs.connect_to_region("us-east-1")
queue = conn2.get_queue('archit_bansal_queue')
while 1 :
    rs = queue.get_messages()
    print len(rs)
    if len(rs) > 0 :
        m = rs[0].get_body()
        x = m
        x = json.loads(x)
        z =  [x["Parameters"].encode('utf-8'),x["IpAddress"], x["Time to respond"], x["Timestamp"],x["Url"], x["Response Code"]]
        z = (', '.join('\'' + item + '\'' for item in z))
        qry = "INSERT INTO Parameters2(`Parameters`,`IpAddress`,`Time to respond`,`Timestamp`,`Url`,`Response Code`) VALUES (" + z + ");"
        cur.execute(qry)
        conn.commit()


