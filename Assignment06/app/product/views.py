from django.http import HttpResponse, JsonResponse
from django.db.models import Q
from django.db.models import F
from models import Products
from models import Orders
from models import Bridge2
from django.db.models import Count
from django.db.models import Sum, ExpressionWrapper, IntegerField, FloatField
import models
import datetime
import json

def index(request):
    return HttpResponse("HERE")

def sales(request):
    startDate = request.GET.get('startDate', '01/31/2000')
    startDate = datetime.datetime.strptime(startDate,'%m/%d/%Y').strftime('%Y-%m-%d')
    endDate = request.GET.get('endDate', '01/31/2050')
    endDate = datetime.datetime.strptime(endDate, '%m/%d/%Y').strftime('%Y-%m-%d')
    print startDate
    print endDate
    orders = Orders.objects.filter(Q(orderdate__gte=startDate) & Q(orderdate__lte=endDate)).aggregate(Count('orderdate'))
    print orders
    query2 = Bridge2.objects.filter(orderid__orderdate__gte=startDate, orderid__orderdate__lte=endDate).values('orderid__orderdate').annotate(orders=ExpressionWrapper(Count('orderid__orderid'),output_field=FloatField()), qty=ExpressionWrapper(Sum('quantity'),output_field=FloatField()), sale_price=ExpressionWrapper(Sum(F('price') * F('quantity')),output_field=FloatField()), buy_price=ExpressionWrapper(Sum(F('quantity') * F('productid__buyprice')),output_field=FloatField()), profit=ExpressionWrapper(Sum((F('price') - F('productid__buyprice'))*F('quantity')),output_field=FloatField())).order_by('-orderid__orderdate')
    dlist2 = []
    for x in query2:
        dlist = {}
        startDate = datetime.datetime.strptime(str(x["orderid__orderdate"]), '%Y-%m-%d').strftime('%m/%d/%Y')
        dlist["date"] = str(startDate)
        dlist["orders"] = x["orders"]
        dlist["qty"] = x["qty"]
        dlist["buy_price"] = x["buy_price"]
        dlist["sale_price"] = x["sale_price"]
        dlist["profit"] = x["profit"]
        dlist2.append(dlist)
    dlist3 = dict()
    dlist3["data"] = dlist2
    return JsonResponse(dlist3)
