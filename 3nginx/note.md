https://cloud.tencent.com/developer/article/1661636


默认的配置有一个问题, 在非首页的路由页面刷新就会报404错误
我们使用 react-router 作为路由管理，在开发端的express服务器下运行和测试表现均正常，部署到线上的nginx服务器后，还需要对该应用在nginx的配置里作相应调整，否则浏览器将不能正常刷新，表现为页面不显示或页面跳转错误等异常。原因在于这些react应用在运行时会更改浏览器uri而又不真的希望服务器对这些uri去作响应，如果此时刷新浏览器，服务器收到浏览器发来的uri就去寻找资源，这个uri在服务器上是没有对应资源，结果服务器因找不到资源就发送403错误标志给浏览器。所以，我们要做的调整是：浏览器在使用这个react应用期间，无论uri更改与否，服务器都发回index.html这个页面就行。


https://www.cnblogs.com/sk-3/p/14847196.html



https://www.digitalocean.com/community/tutorials/deploy-react-application-with-nginx-on-ubuntu