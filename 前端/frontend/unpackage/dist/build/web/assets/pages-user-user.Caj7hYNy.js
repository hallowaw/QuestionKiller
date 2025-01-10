import{r as e,k as o,s as a,B as t,C as l,n as s,d as n,w as c,i,o as d,e as r,f as u,t as p,g as k,q as m,h,I as f,j as g,u as _}from"./index-iGyM-cqh.js";import{_ as w}from"./_plugin-vue_export-helper.BCo6x5W8.js";const P=w({data:()=>({avatarUrl:"",nickname:"",username:"",showNicknamePopup:!1,newNickname:"",showAddBookPopup:!1,newBook:{title:"",description:"",isPublic:0}}),created(){this.fetchAvatarUrl(),this.fetchUserInfo(),console.log("isPublic 初始化:",this.newBook.isPublic,typeof this.newBook.isPublic)},methods:{fetchAvatarUrl(){e({url:"http://localhost:9192/user/getAvatarUrl",method:"GET",header:{token:o("token")},success:e=>{1===e.data.code?(this.avatarUrl=e.data.data,console.log("头像地址获取成功:",this.avatarUrl)):(console.error("获取头像失败:",e.data.msg),a({title:e.data.msg||"获取头像失败",icon:"none"}))},fail:e=>{console.error("网络请求失败:",e),a({title:"网络错误，请重试",icon:"none"})}})},chooseAvatar(){t({count:1,sizeType:["compressed"],sourceType:["album","camera"],success:e=>{const o=e.tempFilePaths[0];this.uploadAvatar(o)}})},uploadAvatar(e){l({url:"http://localhost:9192/user/upload",filePath:e,name:"image",header:{token:o("token")},success:e=>{const o=JSON.parse(e.data);1===o.code?(a({title:"上传成功",icon:"none"}),this.fetchAvatarUrl()):(console.error("上传失败:",o.msg),a({title:o.msg||"上传失败",icon:"none"}))},fail:e=>{console.error("网络请求失败:",e),a({title:"网络错误，请重试",icon:"none"})}})},fetchUserInfo(){e({url:"http://localhost:9192/user/getNicknameAndUsername",method:"GET",header:{token:o("token")},success:e=>{1===e.data.code?(this.nickname=e.data.data.nickname,this.username=e.data.data.username):a({title:e.data.msg||"获取用户信息失败",icon:"none"})},fail:e=>{a({title:"网络错误，请重试",icon:"none"})}})},setNickname(){this.newNickname=this.nickname,this.showNicknamePopup=!0},closeNicknamePopup(){this.showNicknamePopup=!1},confirmNicknameChange(){this.newNickname.trim()?e({url:`http://localhost:9192/user/updateNickname?nickname=${encodeURIComponent(this.newNickname.trim())}`,method:"PUT",header:{token:o("token")},success:e=>{1===e.data.code?(this.nickname=this.newNickname.trim(),a({title:"昵称修改成功",icon:"none"})):a({title:e.data.msg||"修改失败",icon:"none"})},fail:e=>{a({title:"网络错误，请重试",icon:"none"})}}):a({title:"请输入有效昵称",icon:"none"}),this.closeNicknamePopup()},toggleIsPublic(e){this.newBook.isPublic=e},addBook(){this.newBook={title:"",description:"",isPublic:0},this.$nextTick((()=>{this.showAddBookPopup=!0}))},confirmAddBook(){const{title:t,description:l,isPublic:s}=this.newBook;t.trim()&&l.trim()?e({url:"http://localhost:9192/wordBook/add",method:"POST",header:{token:o("token"),"Content-Type":"application/json"},data:{name:t,description:l,isPublic:s},success:e=>{1===e.data.code?(a({title:"添加成功",icon:"none"}),this.closeAddBookPopup()):a({title:e.data.msg||"添加失败",icon:"none"})},fail:e=>{a({title:"网络错误，请重试",icon:"none"})}}):a({title:"请输入完整信息",icon:"none"})},closeAddBookPopup(){this.showAddBookPopup=!1},goToWordBookPage(){s({url:"/pages/wordBookPage/wordBookPage"})},goToImportWordBookPage(){s({url:"/pages/importWordBookPage/importWordBookPage"})},goToShowStats(){s({url:"/pages/ShowStats/ShowStats"})},goToLogin(){s({url:"/pages/login/login"})}}},[["render",function(e,o,a,t,l,s){const w=m,P=h,B=i,C=f,b=g,v=_;return d(),n(B,{class:"container"},{default:c((()=>[r(B,{class:"profile-section"},{default:c((()=>[r(B,{class:"avatar",onClick:s.chooseAvatar},{default:c((()=>[l.avatarUrl?(d(),n(w,{key:0,src:l.avatarUrl,mode:"aspectFill",class:"avatar-img"},null,8,["src"])):(d(),n(P,{key:1,class:"placeholder"},{default:c((()=>[u("+")])),_:1}))])),_:1},8,["onClick"]),r(B,{class:"text-section"},{default:c((()=>[r(B,{class:"label-and-value"},{default:c((()=>[r(P,{class:"label nickname-label"},{default:c((()=>[u("昵称:")])),_:1}),r(B,{class:"nickname",onClick:s.setNickname},{default:c((()=>[u(p(l.nickname||"设置昵称"),1)])),_:1},8,["onClick"])])),_:1}),r(B,{class:"label-and-value"},{default:c((()=>[r(P,{class:"label username-label"},{default:c((()=>[u("用户名:")])),_:1}),r(B,{class:"username"},{default:c((()=>[u(p(l.username||"用户名"),1)])),_:1})])),_:1})])),_:1})])),_:1}),l.showNicknamePopup?(d(),n(B,{key:0,class:"popup-overlay"},{default:c((()=>[r(B,{class:"popup-container"},{default:c((()=>[r(B,{class:"popup-title"},{default:c((()=>[u("修改昵称")])),_:1}),r(C,{modelValue:l.newNickname,"onUpdate:modelValue":o[0]||(o[0]=e=>l.newNickname=e),placeholder:"请输入新的昵称",class:"popup-input"},null,8,["modelValue"]),r(b,{class:"register-btn",onClick:s.confirmNicknameChange},{default:c((()=>[u("确认修改")])),_:1},8,["onClick"]),r(B,{class:"close-btn",onClick:s.closeNicknamePopup},{default:c((()=>[u("×")])),_:1},8,["onClick"])])),_:1})])),_:1})):k("",!0),l.showAddBookPopup?(d(),n(B,{key:1,class:"popup-overlay"},{default:c((()=>[r(B,{class:"popup-container"},{default:c((()=>[r(B,{class:"popup-title"},{default:c((()=>[u("添加词书")])),_:1}),r(C,{modelValue:l.newBook.title,"onUpdate:modelValue":o[1]||(o[1]=e=>l.newBook.title=e),placeholder:"请输入词书标题",class:"popup-input"},null,8,["modelValue"]),r(C,{modelValue:l.newBook.description,"onUpdate:modelValue":o[2]||(o[2]=e=>l.newBook.description=e),placeholder:"请输入词书描述",class:"popup-input"},null,8,["modelValue"]),r(B,{class:"checkbox-container"},{default:c((()=>[r(v,{checked:1===l.newBook.isPublic,onChange:o[3]||(o[3]=e=>s.toggleIsPublic(1))},{default:c((()=>[u("公开")])),_:1},8,["checked"]),r(v,{checked:0===l.newBook.isPublic,onChange:o[4]||(o[4]=e=>s.toggleIsPublic(0))},{default:c((()=>[u("私密")])),_:1},8,["checked"])])),_:1}),r(b,{class:"register-btn",onClick:s.confirmAddBook},{default:c((()=>[u("确认添加")])),_:1},8,["onClick"]),r(B,{class:"close-btn",onClick:s.closeAddBookPopup},{default:c((()=>[u("×")])),_:1},8,["onClick"])])),_:1})])),_:1})):k("",!0),r(B,{class:"list-item",onClick:s.goToWordBookPage},{default:c((()=>[r(P,{class:"item-text"},{default:c((()=>[u("删改查和修改默认词书")])),_:1}),r(P,{class:"arrow"},{default:c((()=>[u(">")])),_:1})])),_:1},8,["onClick"]),r(B,{class:"list-item",onClick:s.addBook},{default:c((()=>[r(P,{class:"item-text"},{default:c((()=>[u("添加词书")])),_:1}),r(P,{class:"arrow"},{default:c((()=>[u(">")])),_:1})])),_:1},8,["onClick"]),r(B,{class:"list-item",onClick:s.goToImportWordBookPage},{default:c((()=>[r(P,{class:"item-text"},{default:c((()=>[u("导入词书")])),_:1}),r(P,{class:"arrow"},{default:c((()=>[u(">")])),_:1})])),_:1},8,["onClick"]),r(B,{class:"list-item",onClick:s.goToShowStats},{default:c((()=>[r(P,{class:"item-text"},{default:c((()=>[u("数据统计")])),_:1}),r(P,{class:"arrow"},{default:c((()=>[u(">")])),_:1})])),_:1},8,["onClick"]),r(B,{class:"list-item",onClick:s.goToLogin},{default:c((()=>[r(P,{class:"item-text"},{default:c((()=>[u("回到登录页面")])),_:1}),r(P,{class:"arrow"},{default:c((()=>[u(">")])),_:1})])),_:1},8,["onClick"])])),_:1})}],["__scopeId","data-v-4a98f4cb"]]);export{P as default};
