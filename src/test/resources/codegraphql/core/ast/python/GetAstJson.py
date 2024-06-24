from ast2json import ast2json
import json
import ast

stmt = '''
if not fullpath.startswith(base_path):
        raise SecurityException()
'''
json_ast = json.dumps(ast2json(ast.parse(stmt).body[0]))
print(json_ast)