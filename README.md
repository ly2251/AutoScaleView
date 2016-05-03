# AutoScaleView

>一个简单易用的全适配解决方案，根据UI设计图，设计定一个模板，通过注入ViewGroup ，在onMeasure之前，对所有的ChildView参数进行等比换算，以此达到全适配的目的

 1. 需要在Application 中初始化 AutoScaleUtil
 2. 所有包含在ViewGroup 中的View都会进行缩放
 3. 目前只能注入到ViewGroup中
