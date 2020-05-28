import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Hello from '@/components/Hello'
import ElementUIDemo from '@/components/ElementUIDemo'
import mytable from '../components/mytable';

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/hello',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/elementUI',
      name: 'ElementUIDemo',
      component: ElementUIDemo
    },
    {
      path: '/mytable',
      name: 'mytable',
      component: mytable
    }
  ]
})
