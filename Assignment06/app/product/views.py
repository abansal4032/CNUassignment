from django.http import HttpResponse, JsonResponse
from django.db.models import Q
from django.db.models import F
from models import Products
from models import Orders
from models import Users
from models import Bridge2
from django.db.models import Count
from django.db.models import Sum, ExpressionWrapper, IntegerField, FloatField, CharField
from serializers import *
import models
import datetime
import json
from rest_framework import viewsets, mixins, status
from rest_framework.decorators import detail_route

class LogMiddleware( object ):

    def process_response( self, request, response ):
        response._container = ['{"data":' + response._container[0] + '}']
        return response



class SummaryViewSet(mixins.ListModelMixin,viewsets.GenericViewSet):
    def get_queryset(self):
        return Products.objects.filter(enabled=1)

    def list(self, request, *args, **kwargs):
        groupBy = request.GET.get("group_by", None)
        code = request.GET.get("code", None)
        categoryName = request.GET.get("category_name", None)
        if groupBy == "category":
            result = self.get_queryset()
            if code:
                result = result.filter(productcode = code)
            if categoryName:
                result = result.filter(categoryid__categorydescription = categoryName)
            result = result.values('categoryid__categoryid').annotate(count=ExpressionWrapper(Count('productid'),output_field=IntegerField()))
            dlist2 = []
            for item in result:
                dlist = {}
                dlist["category_id"] = item["categoryid__categoryid"]
                dlist["count"] = item["count"]
                dlist2.append(dlist)
            if len(dlist2) == 0:
                dlist = {}
                dlist["count"] = 0
                dlist2.append(dlist)
            return JsonResponse(dlist2, safe = False)
        else:
            result = self.get_queryset()
            if code:
                result = result.filter(productcode = code)
            result = result.aggregate(count=Count('productid'))
            dlist2 = []
            dlist = {}
            dlist["count"] = result["count"]
            dlist2.append(dlist)
            return JsonResponse(dlist2, safe = False)

class OrderSummaryViewSet(mixins.ListModelMixin,viewsets.GenericViewSet):
    queryset=Bridge2.objects.all()

    def list(self, request, *args, **kwargs):
        groupBy = request.GET.get("group_by", None)
        productCode = request.GET.get("orderlineitem__product__code", None)
        categoryName = request.GET.get("orderlineitem__product__category__name", None)
        result = self.get_queryset()
        if productCode :
            result = result.filter(productid__productcode = productCode)
        if categoryName :
            result = result.filter(productid__categoryid__categorydescription = categoryName)
        if groupBy == "product":
            result = result.values('productid__productid').annotate(count=ExpressionWrapper(Count('orderid__orderid'),output_field=IntegerField()))
            dlist2 = []
            for item in result:
                dlist = {}
                dlist["product_id"] = item["productid__productid"]
                dlist["count"] = item["count"]
                dlist2.append(dlist)
            return JsonResponse(dlist2, safe = False)
        elif groupBy == "category":
            result = result.values('productid__categoryid__categoryid').annotate(count=ExpressionWrapper(Count('orderid__orderid'),output_field=IntegerField()))
            dlist2 = []
            for item in result:
                dlist = {}
                dlist["count"] = item["count"]
                dlist["category_id"] = item["productid__categoryid__categoryid"]
                dlist2.append(dlist)
            return JsonResponse(dlist2, safe = False)
        else :
            result = result.aggregate(count=Count('orderid__orderid'))
            dlist2 = []
            dlist = {}
            dlist["count"] = result["count"]
            dlist2.append(dlist)
            return JsonResponse(dlist2, safe = False)


class ProductViewSet(viewsets.ModelViewSet):
    serializer_class = ProductSerializer
    def get_queryset(self):
        return Products.objects.filter(enabled=1)

    def destroy(self, request, *args, **kwargs):
        Products.objects.filter(productid=kwargs["pk"]).update(enabled=0)
        return HttpResponse(status.HTTP_404_NOT_FOUND)


class OrderViewSet(viewsets.ModelViewSet):
    serializer_class = OrderSerializer
    def get_queryset(self):
        return Orders.objects.filter(enabled=1)

    def destroy(self, request, *args, **kwargs):
        Orders.objects.filter(orderid=kwargs["pk"]).update(enabled=0)
        return HttpResponse(status.HTTP_404_NOT_FOUND)


class OrderLineItemSet(mixins.CreateModelMixin,mixins.ListModelMixin,mixins.RetrieveModelMixin,viewsets.GenericViewSet):
    serializer_class = OrderLineItemSerializer
    def get_queryset(selfself):
        return OrderLineItemSet.objects.all()

    def list(self, request, *args, **kwargs):
        orderLineItem = Bridge2.objects.filter(orderid=kwargs["order_id"])
        serializer = self.get_serializer(orderLineItem, many=True)
        return JsonResponse(serializer.data, safe = False)

    def retrieve(self, request, *args, **kwargs):
        orderLineItem = Bridge2.objects.filter(id=kwargs["pk"])
        serializer = self.get_serializer(orderLineItem, many=True)
        return JsonResponse(serializer.data, safe = False)

    def create(self, request, *args, **kwargs):
        product = Products.objects.filter(productid = request.POST.get("product_id"))
        order = Orders.objects.filter(orderid = kwargs["order_id"])
        orderLineItem = Bridge2.objects.create(
            productid = product[0],
            orderid = order[0],
            price = request.POST.get("price"),
        )
        serializer = self.get_serializer(orderLineItem)
        return JsonResponse(serializer.data, safe = False)



    def get_queryset(self):
        return Orders.objects.all()



def sales(request):
    startDate = request.GET.get('startDate', '01/31/2000')
    startDate = datetime.datetime.strptime(startDate,'%m/%d/%Y').strftime('%Y-%m-%d')
    endDate = request.GET.get('endDate', '01/31/2050')
    endDate = datetime.datetime.strptime(endDate, '%m/%d/%Y').strftime('%Y-%m-%d')
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
