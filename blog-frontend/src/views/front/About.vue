<!--
  关于页面
  功能：展示博主个人介绍、技术栈、联系方式
-->
<template>
  <div class="about-page">
    <div class="card about-card">
      <div class="about-header">
        <el-avatar :size="120" src="/default-avatar.png" />
        <h2>王熙磊</h2>
        <p class="title">资深Java开发工程师</p>
      </div>

      <div class="about-content">
        <h3>个人简介</h3>
        <p>10年+Java开发经验，精通Spring Boot、微服务架构、数据库优化、DevOps等技术领域。曾主导开发多个大型企业级项目，具备丰富的系统设计和团队管理经验。</p>

        <h3>技术栈</h3>
        <div class="tech-stack">
          <el-tag v-for="tech in techStack" :key="tech" type="info" effect="plain">{{ tech }}</el-tag>
        </div>

        <h3>联系方式</h3>
        <div class="contact">
          <p><strong>邮箱：</strong>admin@xilei.com</p>
          <p><strong>GitHub：</strong>https://github.com/xilei</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const techStack = [
  'Java', 'Spring Boot', 'Spring Cloud', 'MyBatis-Plus', 'MySQL',
  'Redis', 'MongoDB', 'Elasticsearch', 'Docker', 'Kubernetes',
  'Nginx', 'Jenkins', 'Vue.js', 'React', 'Python', 'Kafka', 'RabbitMQ'
]
</script>

<style lang="scss" scoped>
.about-card {
  max-width: 800px;
  margin: 0 auto;
}

.about-header {
  text-align: center;
  margin-bottom: 30px;

  h2 {
    margin: 15px 0 5px;
    font-size: 24px;
  }

  .title {
    color: var(--text-secondary);
  }
}

.about-content {
  h3 {
    margin: 20px 0 10px;
    padding-bottom: 8px;
    border-bottom: 2px solid var(--primary-color);
  }

  p {
    line-height: 1.8;
    color: var(--text-regular);
  }
}

.tech-stack {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.contact {
  p {
    margin: 8px 0;
  }
}
</style>
