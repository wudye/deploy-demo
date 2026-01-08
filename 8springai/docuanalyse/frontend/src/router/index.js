import HelloWorld from '@/components/HelloWorld.vue'
import { ro } from 'element-plus/es/locales.mjs'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: HelloWorld,
        meta: {
            title: 'Home Page'
        }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})


router.beforeEach((to, from, next) => {
    document.title = to.meta.title || 'Default Title'
    next()
})
export default router
