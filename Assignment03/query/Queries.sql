#Query1 Total Daily Sale
select OrderDate,sum(price*Quantity) as Sale from Bridge,Orders where Bridge.OrderId = Orders.OrderId group by OrderDate;

#Query2 Total Daily Profit
select OrderDate,sum(price*Quantity) as Sale,sum(Quantity*(select buyPrice from product where product.productCode = Bridge.productCode)) as CostPrice, sum(price*Quantity)-sum(Quantity*(select buyPrice from product where product.productCode = Bridge.productCode)) as Profit from Bridge,Orders where Bridge.OrderId = Orders.OrderId group by OrderDate;

#Query3 Find the average order size per customer
select avg(Quantity), customerName from Bridge, Orders where Orders.OrderId = Bridge.OrderId group by customerName;

#Query4 Delete a product line
-- alter table product add column display boolean default true;   Added a column for marking the product
update product set display = false where productCode = 'S18_1984';

#Query5 Fulfill an order
-- Enclose in a transaction
insert into Orders (customerName,OrderDate,status) values("Alpha Cognac",now(),"shipped");
insert into Bridge values(LAST_INSERT_ID(), "S18_1984", 100, 3344); 