from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask import request
from flask import jsonify
import dateutil.parser as parser
from datetime import datetime

app = Flask(__name__)
f = open("property.txt")
str = f.readline()
app.config['SQLALCHEMY_DATABASE_URI'] = str
db = SQLAlchemy(app)

def convertDateToIso(date):
    ret = parser.parse(date)
    return ret.isoformat()

class audit(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    timestamp = db.Column(db.String(120))
    url = db.Column(db.String(80))
    request_type = db.Column(db.String(80))
    parameters = db.Column(db.String(80))
    request_duration_ms = db.Column(db.Integer)
    response_code = db.Column(db.Integer)
    request_ip_address = db.Column(db.String(120))

@app.route('/api/auditLogs', methods=['GET'])
def get_logs_params():




     start_time = request.args.get('startTime', '01/01/2010T00:00:00')
     end_time = request.args.get('endTime', '01/01/2050T00:00:00')
     offset_no = request.args.get('offsetNo', 0)
     max_limit = request.args.get('limit', 10)

     fmt = '%m/%d/%YT%H:%M:%S'
     start_time = datetime.strptime(start_time, fmt)

     fmt = '%m/%d/%YT%H:%M:%S'
     end_time = datetime.strptime(end_time, fmt)

     if int(offset_no) < 0:
         offset_no = 0

     if int(max_limit) > 10:
         max_limit = 10

     if int(max_limit) < 0:
         max_limit = 0

     users = audit.query.order_by(audit.timestamp.desc()).filter(audit.timestamp.between(start_time, end_time)).limit(max_limit).offset(offset_no)

     dlist = []
     for x in users:
         dict_obj = dict()
         dict_obj["id"] = x.id
         dict_obj["timestamp"] = x.timestamp.strftime("%m/%d/%YT%H:%M:%S");
         dict_obj["response_code"] = x.response_code
         dict_obj["request_type"] = x.request_type
         dict_obj["response_duration_ms"] = x.request_duration_ms
         dict_obj["parameters"] = x.parameters
         dict_obj["url"] = x.url
         dlist.append(dict_obj)
     dlist2 = dict()
     dlist2["body"] = dlist
     return jsonify(dlist2)

def alc2json(row):
    return dict([(col, str(getattr(row,col))) for col in row.__table__.columns.keys()])

if __name__ == '__main__':
   app.run()

