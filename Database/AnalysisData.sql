#Without adding foreign key

select orderDate,sum(price*quantity) as Sale from OrderLineMillion,OrdersMillion where OrderLineMillion.orderId = OrdersMillion.orderId group by orderDate;
10 s

select orderDate,sum(price*quantity) as Sale,sum(quantity*(select buyPrice from products where products.productId = OrderLineMillion.productId)) as CostPrice, sum(price*quantity)-sum(quantity*(select buyPrice from products where products.productId = OrderLineMillion.productId)) as Profit from OrderLineMillion,OrdersMillion where OrderLineMillion.orderId = OrdersMillion.orderId group by orderDate;
99 s

select avg(quantity), userId from OrderLineMillion, OrdersMillion where OrdersMillion.OrderId = OrderLineMillion.OrderId group by userId;
12 s 


alter table OrderLineMillion
add foreign key (orderId)
references OrdersMillion(orderId);

alter table OrderLineMillion
add foreign key (productId)
references products(productId);

#After adding foreign key

select orderDate,sum(price*quantity) as Sale from OrderLineMillion,OrdersMillion where OrderLineMillion.orderId = OrdersMillion.orderId group by orderDate;
6.3 s

select orderDate,sum(price*quantity) as Sale,sum(quantity*(select buyPrice from products where products.productId = OrderLineMillion.productId)) as CostPrice, sum(price*quantity)-sum(quantity*(select buyPrice from products where products.productId = OrderLineMillion.productId)) as Profit from OrderLineMillion,OrdersMillion where OrderLineMillion.orderId = OrdersMillion.orderId group by orderDate;
12.4 s

select avg(quantity), userId from OrderLineMillion, OrdersMillion where OrdersMillion.OrderId = OrderLineMillion.OrderId group by userId;
7 s


create index orderDate on OrdersMillion (orderDate);

#After creating index

select orderDate,sum(price*quantity) as Sale from OrderLineMillion,OrdersMillion where OrderLineMillion.orderId = OrdersMillion.orderId group by orderDate;
4.24 s

select orderDate,sum(price*quantity) as Sale,sum(quantity*(select buyPrice from products where products.productId = OrderLineMillion.productId)) as CostPrice, sum(price*quantity)-sum(quantity*(select buyPrice from products where products.productId = OrderLineMillion.productId)) as Profit from OrderLineMillion,OrdersMillion where OrderLineMillion.orderId = OrdersMillion.orderId group by orderDate;
10.9 s

select avg(quantity), userId from OrderLineMillion, OrdersMillion where OrdersMillion.OrderId = OrderLineMillion.OrderId group by userId;
3.58 s
