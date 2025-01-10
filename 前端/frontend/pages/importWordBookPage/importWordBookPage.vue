<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input v-model="searchName" placeholder="请输入词书名称" class="search-input" />
      <button @tap="searchWordBooks" class="search-btn primary">搜索</button>
      <input v-model="shareCode" placeholder="请输入4位数分享码" class="share-input" />
      <button @tap="importByShareCode" class="search-btn secondary">导入</button>
    </view>

    <!-- 数据展示区域 -->
    <view v-if="wordBooks.length > 0" class="list">
      <view v-for="item in wordBooks" :key="item.id" class="list-item">
        <view class="item-info">
          <text class="item-name selectable">{{ item.name }}</text>
          <text class="item-desc selectable">
            {{ item.description.length > 50 ? item.description.substring(0, 10) + '...' : item.description }}
          </text>
        </view>
        <view class="item-code-import">
          <text class="item-code selectable">{{ item.shareCode }}</text>
          <button @tap="importBook(item.shareCode)" class="import-btn">一键导入</button>
        </view>
      </view>
    </view>

    <view v-else class="no-data">暂无数据</view>

    <!-- 分页按钮 -->
<view class="pagination">
  <button :disabled="page <= 1" @tap="changePage(page - 1)" class="pagination-btn">上一页</button>
  <input v-model.number="jumpPage" class="page-input" />
  <button @tap="changePage(jumpPage)" class="pagination-btn">跳转</button>
  <button :disabled="page >= totalPages" @tap="changePage(page + 1)" class="pagination-btn">下一页</button>
  <text class="total-pages">共 {{ totalPages }} 页</text>
</view>

  </view>
</template>

<script>
export default {
  data() {
    return {
      searchName: '',
      shareCode: '',
      wordBooks: [],
      page: 1,
      pageSize: 10,
      totalPages: 1,
      jumpPage: 1,
      token: uni.getStorageSync('token'),
    };
  },
  methods: {
    // 获取词书列表
    async fetchWordBooks() {
      const res = await uni.request({
        url: `http://localhost:9192/wordBook/publicOthersWordBookPageQuery?pageSize=${this.pageSize}&page=${this.page}&name=${this.searchName}`,
        method: 'GET',
        header: { token: this.token },
      });
      if (res.data.code === 1 && res.data.data) {
        this.wordBooks = res.data.data.records;
        this.totalPages = Math.ceil(res.data.data.total / this.pageSize);
      } else {
        uni.showToast({ title: res.data.msg || '查询失败', icon: 'none' });
      }
    },

    // 搜索词书
    searchWordBooks() {
      this.page = 1;
      this.fetchWordBooks();
    },

    // 根据分享码导入词书
    async importByShareCode() {
      const res = await uni.request({
        url: `http://localhost:9192/wordBook/addWordBookByShareCode/${this.shareCode}`,
        method: 'POST',
        header: { token: this.token },
      });
      if (res.data.code === 1) {
        uni.showToast({ title: '导入成功', icon: 'success' });
        this.fetchWordBooks();
      } else {
        uni.showToast({ title: res.data.msg || '导入失败', icon: 'none' });
      }
    },

    // 一键导入词书
    async importBook(shareCode) {
      const res = await uni.request({
        url: `http://localhost:9192/wordBook/addWordBookByShareCode/${shareCode}`,
        method: 'POST',
        header: { token: this.token },
      });
      if (res.data.code === 1) {
        uni.showToast({ title: '导入成功', icon: 'success' });
        this.fetchWordBooks();
      } else {
        uni.showToast({ title: res.data.msg || '导入失败', icon: 'none' });
      }
    },

    // 分页切换
    changePage(newPage) {
      if (newPage >= 1 && newPage <= this.totalPages) {
        this.page = newPage;
        this.fetchWordBooks();
      } else {
        uni.showToast({ title: '页码超出范围', icon: 'none' });
      }
    },
  },
  onLoad() {
    this.fetchWordBooks();
  },
};
</script>


<style>
/* 容器样式 */
.container {
  padding: 16px;
  background-color: #f4f8fc;
}

/* 搜索栏样式 */
.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  gap: 8px;
  padding: 8px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-input {
  flex: 7; /* 70% */
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.share-input {
  flex: 3; /* 30% */
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-btn {
  padding: 10px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}

.search-btn.primary {
  background-color: #007bff;
  color: white;
}

.search-btn.secondary {
  background-color: #6c757d;
  color: white;
}

/* 列表样式 */
.list {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #e9ecef;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-weight: bold;
  color: #343a40;
}

.item-desc {
  font-size: 14px;
  color: #6c757d;
}

.item-code-import {
  display: flex;
  align-items: center;
  gap: 8px;
}

.import-btn {
  padding: 8px 12px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
  gap: 8px;
}

.pagination-btn {
  padding: 8px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination-btn:disabled {
  background-color: #b0c4de;
}

.page-input {
  width: 50px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 4px;
}

/* 空数据样式 */
.no-data {
  text-align: center;
  padding: 16px;
  color: #6c757d;
}
.total-pages {
  margin-left: 8px;
  font-size: 30px;
  color: #6c757d;
}

</style>