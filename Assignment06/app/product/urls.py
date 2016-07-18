from django.conf.urls import url, include
from rest_framework import routers
from . import views
from views import *

router = routers.DefaultRouter()
router.register(r'products/summary', SummaryViewSet, base_name = 'SummaryViewSet')
router.register(r'orders/summary', OrderSummaryViewSet, base_name = 'OrderSummaryViewSet')
router.register(r'^products', ProductViewSet, base_name = 'ProductViewSet')
router.register(r'^orders', OrderViewSet, base_name = 'OrderViewSet')
router.register(r'orders/(?P<order_id>[0-9]+)/orderLineItem', OrderLineItemSet, base_name = 'OrderLineItemSet')


urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api/reports/daily-sale$', views.sales, name='sales'),
]