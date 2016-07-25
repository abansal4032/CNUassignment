import understand
db = understand.open("../testdb/test_sonic.udb")

methods = db.ents("methods")

for item in methods:
    if(item.name().endswith('run')):
        caller = item.refs('Java Callby')
        parent = item.ref('DefineIn').ent()
        parentRelations = [item.ent() for item in parent.refs('Couple')]

        if('Thread' in parentRelations):
            for item in caller:
                method = item.ent()
                filename = item.file()
                details = (item.line(), item.column())

                caller = method.ref('DefineIn').ent()
                print('Thread.run() detected in ' + method.longname() + ' in ' + filename + ' at ' + details + 'Did you intend to use Thread.run() to create a new execution thread?')
			
		else if('Runnable' in parentRelations):
        	for item in caller:
            	method = item.ent()
            	filename = item.file()
            	details = (item.line(), item.column())

            	caller = method.ref('DefineIn').ent()
            	print('Runnale.run() detected in ' + method.longname() + ' in ' + filename + ' at ' + details + 'Did you intend to use Runnable.run() to create a new execution thread?')