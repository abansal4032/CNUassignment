from rest_framework import serializers
from models import *
import datetime

class OrderLineItemSerializer(serializers.ModelSerializer):

    id = serializers.IntegerField(read_only = True)
    product_id = serializers.IntegerField(source = 'productid.productid')
    order_id = serializers.IntegerField(source = 'orderid.orderid', read_only = True)
    price = serializers.DecimalField(max_digits=10, decimal_places=2)

    class Meta:
        model = Bridge2
        fields = ('id', 'product_id', 'order_id', 'price')

class ProductSerializer(serializers.ModelSerializer):

    id = serializers.IntegerField(read_only = True, source='productid')
    code = serializers.CharField(max_length = 255, source='productcode')
    description = serializers.CharField(max_length = 255,source='productdescription')
    price = serializers.DecimalField(max_digits=10, decimal_places=2,source='buyprice')
    category_id = serializers.IntegerField(source='categoryid.categoryid',read_only=True)
    category = serializers.CharField(source='categoryid.categorydescription',write_only=True,max_length = 255)
    enabled = serializers.IntegerField(read_only = True)

    def create(self, validated_data):
        category = Category.objects.get_or_create(
            categorydescription = validated_data['categoryid']['categorydescription'],
        )

        product = Products.objects.create(
            productcode = validated_data['productcode'],
            productdescription = validated_data['productdescription'],
            buyprice = validated_data['buyprice'],
            categoryid = category[0],
            enabled = 1
        )

        return product

    class Meta:
        model = Products
        fields = ('id', 'code', 'description', 'price', 'category_id', 'category', 'enabled')

    def update(self, instance, validated_data):
        instance.productcode = validated_data.get('productcode',instance.productcode)
        instance.productdescription = validated_data.get('productdescription', instance.productdescription)
        instance.buyprice = validated_data.get('buyprice', instance.buyprice)
        if 'categoryid' in validated_data.keys():
            category = Category.objects.get_or_create(categorydescription = validated_data['categoryid'].get('categorydescription',instance.categoryid.categorydescription))
            instance.categoryid = category[0]
        instance.save()
        return instance

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users

class Bridge2Serializer(serializers.ModelSerializer):
    id = serializers.IntegerField(read_only=True)
    product_id = serializers.IntegerField(source='productid', read_only=True)
    order_id = serializers.IntegerField(source='orderid', read_only = True)
    price = serializers.DecimalField(max_digits=10, decimal_places=2)
    class Meta:
        model = Users

class OrderSerializer(serializers.ModelSerializer):
    id = serializers.IntegerField(read_only=True,source='orderid')
    status = serializers.CharField(max_length = 255, required = False)
    username = serializers.CharField(source='userid.customername', max_length = 255, required = False)
    address = serializers.CharField(source='userid.addressline1', max_length = 255, required = False)

    def create(self, validated_data):
        if 'userid' in validated_data.keys():
            if 'customername' in validated_data['userid'].keys():
                customername = validated_data['userid']['customername']
                user = Users.objects.get_or_create(
                    customername=customername,
                )
            if 'addressline1' in validated_data['userid'].keys():
                addressline1 = validated_data['userid']['addressline1']
                Users.objects.filter(customername=validated_data['userid']['customername']).update(addressline1=addressline1)
                user = Users.objects.filter(customername=validated_data['userid']['customername'])

            order = Orders.objects.create(
                orderdate=datetime.datetime.now().date(),
                status=validated_data['status'],
                userid=user[0],
                enabled=1
            )
        else :
            customername = ''
            addressline1 = ''
            user = Users.objects.get_or_create(
                customername = customername,
                addressline1 = addressline1,
            )
            order = Orders.objects.create(
                orderdate=datetime.datetime.now().date(),
                status=validated_data['status'],
                userid=user[0],
                enabled=1
            )
        return order

    def partial_update(self, instance, validated_data):
        keyUser = instance.userid.userid
        if 'status' in validated_data.keys():
            instance.status = validated_data['status']
        if 'userid' in validated_data.keys():
            if 'customername' in validated_data['userid'].keys():
                Users.objects.filter(userid=keyUser).update(customername=validated_data['userid']['customername'])
                instance.userid.customername = validated_data['userid']['customername']
            if 'addressline1' in validated_data['userid'].keys():
                Users.objects.filter(userid=keyUser).update(addressline1=validated_data['userid']['addressline1'])
                instance.userid.addressline1 = validated_data['userid']['addressline1']
        instance.save()

        return instance


    def update(self, instance, validated_data):
        if self.partial == True :
            return self.partial_update(instance, validated_data)
        keyUser = instance.userid.userid
        if 'status' in validated_data.keys():
            instance.status = validated_data['status']
        else:
            instance.status = None
        if 'userid' in validated_data.keys():
            if 'customername' in validated_data['userid'].keys():
                Users.objects.filter(userid=keyUser).update(customername=validated_data['userid']['customername'])
                instance.userid.customername = validated_data['userid']['customername']
            else:
                Users.objects.filter(userid=keyUser).update(customername=None)
                instance.userid.customername = None
            if 'addressline1' in validated_data['userid'].keys():
                Users.objects.filter(userid=keyUser).update(addressline1=validated_data['userid']['addressline1'])
                instance.userid.addressline1 = validated_data['userid']['addressline1']
            else:
                Users.objects.filter(userid=keyUser).update(addressline1=None)
                instance.userid.addressline1 = None
        else:
            Users.objects.filter(userid=keyUser).update(customername=None)
            Users.objects.filter(userid=keyUser).update(addressline1=None)
            instance.userid.customername = None
            instance.userid.addressline1 = None
        instance.save()

        return instance

    class Meta:
        model = Orders
        fields = ('id', 'status', 'username', 'address')
