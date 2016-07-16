import csv
import random

f = open('assignment03-load.csv', 'r')
f4 = open('order.csv', 'w')
f5 = open('orderline.csv', 'w')

tuple = []
users = set()
names = set()
dates = set()
status = set()
status.add("Shipped")
status.add("Placed")
status.add("Delivered")
with open('assignment03-load.csv', 'r') as csvfile:
	tuple = list(csv.reader(csvfile))
for item in tuple:
	names.add(item[0])
	dates.add(item[10])
names = list(names)
dates = list(dates)
status = list(status)

userLen = 122
datesLen = len(dates) - 1
statusLen = 2
productLen = 100
orderLineId = 270
quantityLen = 100
priceMax = 1000
orderId = 3050
productCode = ''

for i in xrange(1000000):
	userIndex = random.randint(0, userLen)
	datesIndex = random.randint(0, datesLen)
	statusIndex = random.randint(0, statusLen)
	quantity = random.randint(1, quantityLen)
	price = random.randint(1, priceMax)
	productIndex = random.randint(1, productLen)
	orderItem = str(orderId) + ',' + dates[datesIndex] + ',' + status[statusIndex] + ',' + str(userIndex) + ',' + "1" + "\n"
	orderLineItem = str(orderLineId) + ',' + str(orderId) + ',' + productCode + ',' + str(quantity) + ',' + str(price) + ',' + str(productIndex) + "\n"
	orderId = orderId + 1
	orderLineId = orderLineId + 1
	f4.write(orderItem)
	f5.write(orderLineItem)


#orderId,"orderDate","status",userId,enabled
#id,orderId,"productCode",quantity,"price",productId

