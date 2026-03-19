const fs = require('fs');
const p = 'D:/SJJ_biyesheji/bishe/oj-frontend/src/views/post/DiscussionView.vue';
let s = fs.readFileSync(p, 'utf8');

s = s.replace(/\n\s*<section class="discussion-hero">[\s\S]*?<\/section>\s*/m, '\n');

s = s.replace(/\n\s*\.discussion-hero\s*\{[\s\S]*?\n\s*\}\n/g, '\n');
s = s.replace(/\n\s*\.hero-[\w-]+\s*\{[\s\S]*?\n\s*\}\n/g, '\n');

s = s.replace(/\n{3,}/g, '\n\n');

fs.writeFileSync(p, s, 'utf8');
