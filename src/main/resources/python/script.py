# -- coding: utf-8 --
import sys
import ast
import os
import traceback
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf8')

def extract_functions_with_content_and_line_numbers(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        source_code = file.read()

    # 解析源代码为抽象语法树
    tree = ast.parse(source_code)

    # 用于存储函数及其内容和行号范围的字典
    function_dict = {}

    # 遍历抽象语法树
    for node in ast.walk(tree):
        if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef)):
            # 获取函数名
            func_name = node.name
            # 获取函数的起始行号
            start_line = node.lineno
            # 获取函数的结束行号
            end_line = node.end_lineno
            # 提取函数的内容
            source_arr = source_code.split('\n')[start_line - 1:end_line]
            for i in range(len(source_arr)):
                source_arr[i] = str(i + start_line) + ': ' + source_arr[i]
            func_content = '\n'.join(source_arr)

            # 将函数名、内容和行号范围存入字典
            function_dict[func_name] = {
                'content': func_content,
                'line_range': (start_line, end_line)
            }

    return function_dict


def path_list2api_list(api_list, path_list, project_path):
    merged_dict = {}
    for path in path_list:
        merged_dict[path] = extract_functions_with_content_and_line_numbers(path)
    for file_name, func in merged_dict.items():
        for func_name, details in func.items():
#             api_list.append(f"{file_name},{func_name},{details['line_range']},{details['content']}")
            print(f"{os.path.relpath(file_name, project_path)},{func_name},{details['line_range']},{details['content']}".replace("\n", "\\n"))


def project2api_list(api_list, project_path):
    # 遍历project_path, 提取出里面所有以.py结尾的文件，把文件绝对路径存在path_list中
    path_list = []
    for root, dirs, files in os.walk(project_path):
        for file in files:
            if file.endswith('.py'):
                file_path = os.path.join(root, file)
                path_list.append(file_path)
    path_list2api_list(api_list, path_list, project_path)


if __name__ == '__main__':
    project_path = sys.argv[1]
    cwe_id = sys.argv[2]
    api_list = []
    project2api_list(api_list, project_path)
#     print(api_list)
