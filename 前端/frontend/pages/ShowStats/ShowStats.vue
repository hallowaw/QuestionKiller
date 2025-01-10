<template>
	<view class="container">
		<view class="data-item">
			<text class="label">总答题数：</text>
			<text class="value">{{ data.allTotalAnswerCount }}</text>
		</view>
		<view class="data-item">
			<text class="label">正确答题数：</text>
			<text class="value">{{ data.allCorrectAnswerCount }}</text>
		</view>
		<view class="data-item">
			<text class="label">最近答题时间：</text>
			<text class="value">{{ formatDate(data.allLastAnswerTime) }}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				data: {
					allTotalAnswerCount: 0,
					allCorrectAnswerCount: 0,
					allLastAnswerTime: []
				}
			}
		},
		created() {
			this.fetchData();
		},
		methods: {
			fetchData() {
				uni.request({
					url: 'http://localhost:9192/user/showStats',
					method: 'GET',
					header: {
					  token: uni.getStorageSync("token"),
					},
					success: (res) => {
						if (res.data.code === 1) {
							this.data = res.data.data;
						}
					}
				});
			},
			formatDate(timeArray) {
				if (!timeArray || timeArray.length === 0) return '';
				const [year, month, day, hour, minute] = timeArray;
				return `${year}-${month}-${day} ${hour}:${minute}`;
			}
		}
	}
</script>

<style>
.container {
	padding: 20px;
	font-size: 16px;
	color: #333;
}
.data-item {
	margin-bottom: 10px;
	display: flex;
	align-items: center;
}
.label {
	font-weight: bold;
	margin-right: 10px;
}
.value {
	color: #007BFF;
}
</style>
