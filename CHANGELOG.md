# Change Log

## 0.8.4
### New
* 支持UpValue着色
### Fix
* 修复一些对象没有被正常着色的问题

## 0.8.3
### New
* 新的基于语言服务器的代码着色功能

## 0.8.2
### New
* 增加Lua脚本颜色配置
### Fix
* 修复一些Lua代码着色问题

## 0.8.1
### Fix
* Lua诊断可能触发异常

## 0.8.0
### New
* 新增EventBus接口对称调用的诊断
* 新增InputSystem接口对称调用的诊断
* 新增BuffId合法性诊断
* 新增Construct/Destruct父类调用诊断
### Fix
* Lua根目录是项目子目录时Debugger无法找到Lua文件