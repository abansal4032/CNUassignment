# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from __future__ import unicode_literals

from django.db import models



class Category(models.Model):
    categoryid = models.AutoField(db_column='categoryId', primary_key=True)  # Field name made lowercase.
    categorydescription = models.TextField(db_column='categoryDescription', blank=True, null=True)  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'category'

class Users(models.Model):
    userid = models.AutoField(db_column='userId', primary_key=True)  # Field name made lowercase.
    customername = models.CharField(db_column='customerName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    contactfirstname = models.CharField(db_column='contactFirstName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    contactlastname = models.CharField(db_column='contactLastName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    phone = models.CharField(max_length=255, blank=True, null=True)
    addressline1 = models.CharField(db_column='addressLine1', max_length=255, blank=True, null=True)  # Field name made lowercase.
    addressline2 = models.CharField(db_column='addressLine2', max_length=255, blank=True, null=True)  # Field name made lowercase.
    city = models.CharField(max_length=255, blank=True, null=True)
    state = models.CharField(max_length=255, blank=True, null=True)
    postalcode = models.CharField(db_column='postalCode', max_length=255, blank=True, null=True)  # Field name made lowercase.
    country = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'users'



class Orders(models.Model):
    orderid = models.AutoField(db_column='orderId', primary_key=True)  # Field name made lowercase.
    orderdate = models.DateField(db_column='orderDate')  # Field name made lowercase.
    status = models.CharField(max_length=255, blank=True, null=True)
    userid = models.ForeignKey('Users', models.DO_NOTHING, db_column='userId', blank=True, null=True)  # Field name made lowercase.
    enabled = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'Orders'


class Products(models.Model):
    productid = models.AutoField(db_column='productId', primary_key=True)  # Field name made lowercase.
    productcode = models.CharField(db_column='productCode', max_length=255, blank=True, null=True)  # Field name made lowercase.
    productname = models.CharField(db_column='productName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    productdescription = models.TextField(db_column='productDescription', blank=True, null=True)  # Field name made lowercase.
    buyprice = models.DecimalField(db_column='buyPrice', max_digits=10, decimal_places=2)  # Field name made lowercase.
    quantityinstock = models.IntegerField(db_column='quantityInStock')  # Field name made lowercase.
    categoryid = models.ForeignKey(Category, models.DO_NOTHING, db_column='categoryId', blank=True, null=True)  # Field name made lowercase.
    enabled = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'products'


class Bridge(models.Model):
    orderid = models.ForeignKey('Orders', models.DO_NOTHING, db_column='orderId', blank=True, null=True)  # Field name made lowercase.
    productcode = models.CharField(db_column='productCode', max_length=255, blank=True, null=True)  # Field name made lowercase.
    quantity = models.IntegerField(blank=True, null=True)
    price = models.DecimalField(max_digits=10, decimal_places=2, blank=True, null=True)
    productid = models.ForeignKey('Products', models.DO_NOTHING, db_column='productId', blank=True, null=True)  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'Bridge'


class Bridge2(models.Model):
    id = models.AutoField(db_column='id', primary_key=True, null=False)
    orderid = models.ForeignKey('Orders', models.DO_NOTHING, db_column='orderId', blank=True, null=True)  # Field name made lowercase.
    productcode = models.CharField(db_column='productCode', max_length=255, blank=True, null=True)  # Field name made lowercase.
    quantity = models.IntegerField(blank=True, null=True)
    price = models.DecimalField(max_digits=10, decimal_places=2)
    productid = models.ForeignKey('Products', models.DO_NOTHING, db_column='productId', blank=True, null=True)  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'Bridge2'


class Parameters2(models.Model):
    parameters = models.TextField(db_column='Parameters', blank=True, null=True)  # Field name made lowercase.
    ipaddress = models.TextField(db_column='IpAddress', blank=True, null=True)  # Field name made lowercase.
    time_to_respond = models.IntegerField(db_column='Time_to_respond', blank=True, null=True)  # Field name made lowercase.
    timestamp = models.DateTimeField(db_column='Timestamp', blank=True, null=True)  # Field name made lowercase.
    url = models.TextField(db_column='Url', blank=True, null=True)  # Field name made lowercase.
    response_code = models.IntegerField(db_column='Response_Code', blank=True, null=True)  # Field name made lowercase.
    request_type = models.CharField(db_column='Request_type', max_length=11, blank=True, null=True)  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'Parameters2'


class Audit(models.Model):
    timestamp = models.DateTimeField(blank=True, null=True)
    url = models.CharField(max_length=255, blank=True, null=True)
    request_type = models.CharField(max_length=255, blank=True, null=True)
    parameters = models.CharField(max_length=255, blank=True, null=True)
    request_duration_ms = models.IntegerField(blank=True, null=True)
    response_code = models.IntegerField(blank=True, null=True)
    request_ip_address = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'audit'


# class AuditLogs(models.Model):
#     id = models.IntegerField(blank=True, null=True)
#     parameters = models.TextField(db_column='Parameters', blank=True, null=True)  # Field name made lowercase.
#     ipaddress = models.CharField(db_column='IpAddress', max_length=255, blank=True, null=True)  # Field name made lowercase.
#     time_to_respond = models.IntegerField(db_column='Time to respond', blank=True, null=True)  # Field name made lowercase. Field renamed to remove unsuitable characters.
#     timestamp = models.CharField(db_column='Timestamp', max_length=255, blank=True, null=True)  # Field name made lowercase.
#     url = models.CharField(db_column='Url', max_length=255, blank=True, null=True)  # Field name made lowercase.
#     response_code = models.IntegerField(db_column='Response Code', blank=True, null=True)  # Field name made lowercase. Field renamed to remove unsuitable characters.
#
#     class Meta:
#         managed = False
#         db_table = 'audit_Logs'
#

class AuditLog(models.Model):
    accept_language = models.TextField(db_column='accept-language', blank=True, null=True)  # Field renamed to remove unsuitable characters.
    postman_token = models.TextField(db_column='postman-token', blank=True, null=True)  # Field renamed to remove unsuitable characters.
    host = models.TextField(blank=True, null=True)
    connection = models.TextField(blank=True, null=True)
    content_type = models.TextField(db_column='content-type', blank=True, null=True)  # Field renamed to remove unsuitable characters.
    cache_control = models.TextField(db_column='cache-control', blank=True, null=True)  # Field renamed to remove unsuitable characters.
    accept_encoding = models.TextField(db_column='accept-encoding', blank=True, null=True)  # Field renamed to remove unsuitable characters.
    user_agent = models.TextField(db_column='user-agent', blank=True, null=True)  # Field renamed to remove unsuitable characters.
    accept = models.TextField(blank=True, null=True)
    ipaddress = models.TextField(db_column='IpAddress', blank=True, null=True)  # Field name made lowercase.
    time_to_respond = models.TextField(db_column='Time to respond', blank=True, null=True)  # Field name made lowercase. Field renamed to remove unsuitable characters.
    timestamp = models.DateTimeField(db_column='Timestamp', blank=True, null=True)  # Field name made lowercase.
    url = models.TextField(db_column='Url', blank=True, null=True)  # Field name made lowercase.
    response_code = models.TextField(db_column='Response Code', blank=True, null=True)  # Field name made lowercase. Field renamed to remove unsuitable characters.
    test = models.CharField(max_length=11, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'audit_log'


class AuthGroup(models.Model):
    name = models.CharField(unique=True, max_length=80)

    class Meta:
        managed = False
        db_table = 'auth_group'


class AuthGroupPermissions(models.Model):
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)
    permission = models.ForeignKey('AuthPermission', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_group_permissions'
        unique_together = (('group', 'permission'),)


class AuthPermission(models.Model):
    name = models.CharField(max_length=255)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING)
    codename = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'auth_permission'
        unique_together = (('content_type', 'codename'),)


class AuthUser(models.Model):
    password = models.CharField(max_length=128)
    last_login = models.DateTimeField(blank=True, null=True)
    is_superuser = models.IntegerField()
    username = models.CharField(unique=True, max_length=30)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    email = models.CharField(max_length=254)
    is_staff = models.IntegerField()
    is_active = models.IntegerField()
    date_joined = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'auth_user'


class AuthUserGroups(models.Model):
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_groups'
        unique_together = (('user', 'group'),)


class AuthUserUserPermissions(models.Model):
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    permission = models.ForeignKey(AuthPermission, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_user_permissions'
        unique_together = (('user', 'permission'),)



class DjangoAdminLog(models.Model):
    action_time = models.DateTimeField()
    object_id = models.TextField(blank=True, null=True)
    object_repr = models.CharField(max_length=200)
    action_flag = models.SmallIntegerField()
    change_message = models.TextField()
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'django_admin_log'


class DjangoContentType(models.Model):
    app_label = models.CharField(max_length=100)
    model = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'django_content_type'
        unique_together = (('app_label', 'model'),)


class DjangoMigrations(models.Model):
    app = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    applied = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_migrations'


class DjangoSession(models.Model):
    session_key = models.CharField(primary_key=True, max_length=40)
    session_data = models.TextField()
    expire_date = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_session'


class Feedback(models.Model):
    feedbackid = models.AutoField(db_column='feedbackId', primary_key=True)  # Field name made lowercase.
    message = models.TextField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'feedback'


class Product(models.Model):
    productcode = models.CharField(db_column='productCode', primary_key=True, max_length=255)  # Field name made lowercase.
    productname = models.CharField(db_column='productName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    productdescription = models.TextField(db_column='ProductDescription', blank=True, null=True)  # Field name made lowercase.
    buyprice = models.CharField(db_column='buyPrice', max_length=255, blank=True, null=True)  # Field name made lowercase.
    categoryid = models.ForeignKey(Category, models.DO_NOTHING, db_column='CategoryId', blank=True, null=True)  # Field name made lowercase.
    display = models.IntegerField(blank=True, null=True)
    product_code = models.CharField(max_length=255)
    product_description = models.CharField(max_length=255, blank=True, null=True)
    product_name = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'product'



class User(models.Model):
    customername = models.CharField(db_column='customerName', primary_key=True, max_length=255)  # Field name made lowercase.
    contactfirstname = models.CharField(db_column='contactFirstName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    contactlastname = models.CharField(db_column='contactLastName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    phone = models.CharField(max_length=255, blank=True, null=True)
    addressline1 = models.CharField(db_column='addressLine1', max_length=255, blank=True, null=True)  # Field name made lowercase.
    addressline2 = models.CharField(db_column='addressLine2', max_length=255, blank=True, null=True)  # Field name made lowercase.
    city = models.CharField(max_length=255, blank=True, null=True)
    state = models.CharField(max_length=255, blank=True, null=True)
    postalcode = models.CharField(db_column='postalCode', max_length=255, blank=True, null=True)  # Field name made lowercase.
    country = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'user'

