<!--
  404页面
-->
<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
    <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
  </div>
</template>

<style lang="scss" scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  h1 { font-size: 100px; color: var(--primary-color); }
  p { font-size: 20px; color: var(--text-secondary); margin: 20px 0; }
}
</style>
