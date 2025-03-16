

export default class FloatImage {
  constructor(context) {
    context.floatImage = this
    this.context = context
  }

  handleFloatImage(option) {
    const block = document.getElementById('float_image_block')
    var box = document.createElement('div')
    if(option.width) {
      box.style.width = option.width + 'px'
    }
    if(option.height) {
      box.style.height = option.height + 'px'
    }
    if(option.top) {
      box.style.top = option.top + 'px'
    }
    if(option.left) {
      box.style.left = option.left + 'px'
    }
    box.className = 'float-image-box'
    if (option.type) {
      box.setAttribute('data-target-type', 'true')
    } else {
      box.setAttribute('data-target-type', 'false')
    }
    box.setAttribute('data-target-source', option.source)
    box.innerHTML = `<span class="r"></span><span class="l"></span><span class="t"></span><span class="b"></span><span class="br"></span><span class="bl"></span><span class="tr"></span><span class="tl"></span><img class="drag" src="${option.value}"><div class="float-image-toolbar" onclick="removeFloatImage(this)"><i class="el-icon-close"></i></div>`
    block.appendChild(box)
    box.oncontextmenu = function(e) {
      e.stopPropagation()
      return false
    }
    resizeable(box)
    draggable(box)
  }
};

function resizeable(ele) {
  var oDiv = ele
  var aSpan = oDiv.getElementsByTagName('span')
  for (var i = 0; i < aSpan.length; i++) {
    const obj = aSpan[i]
    obj.onmousedown = function(ev) {
      var oEv = ev || event
      oEv.preventDefault()
      var oldWidth = oDiv.offsetWidth
      var oldHeight = oDiv.offsetHeight
      var oldX = oEv.clientX
      var oldY = oEv.clientY
      var oldLeft = oDiv.offsetLeft
      var oldTop = oDiv.offsetTop
      document.onmousemove = function(ev) {
        var oEv = ev || event
        if (obj.className == 'tl') {
          oDiv.style.width = oldWidth - (oEv.clientX - oldX) + 'px'
          oDiv.style.height = oldHeight - (oEv.clientY - oldY) + 'px'
          oDiv.style.left = oldLeft + (oEv.clientX - oldX) + 'px'
          oDiv.style.top = oldTop + (oEv.clientY - oldY) + 'px'
        } else if (obj.className == 'bl') {
          oDiv.style.width = oldWidth - (oEv.clientX - oldX) + 'px'
          oDiv.style.height = oldHeight + (oEv.clientY - oldY) + 'px'
          oDiv.style.left = oldLeft + (oEv.clientX - oldX) + 'px'
          oDiv.style.bottom = oldTop + (oEv.clientY + oldY) + 'px'
        } else if (obj.className == 'tr') {
          oDiv.style.width = oldWidth + (oEv.clientX - oldX) + 'px'
          oDiv.style.height = oldHeight - (oEv.clientY - oldY) + 'px'
          oDiv.style.right = oldLeft - (oEv.clientX - oldX) + 'px'
          oDiv.style.top = oldTop + (oEv.clientY - oldY) + 'px'
        } else if (obj.className == 'br') {
          oDiv.style.width = oldWidth + (oEv.clientX - oldX) + 'px'
          oDiv.style.height = oldHeight + (oEv.clientY - oldY) + 'px'
          oDiv.style.right = oldLeft - (oEv.clientX - oldX) + 'px'
          oDiv.style.bottom = oldTop + (oEv.clientY + oldY) + 'px'
        } else if (obj.className == 't') {
          oDiv.style.height = oldHeight - (oEv.clientY - oldY) + 'px'
          oDiv.style.top = oldTop + (oEv.clientY - oldY) + 'px'
        } else if (obj.className == 'b') {
          oDiv.style.height = oldHeight + (oEv.clientY - oldY) + 'px'
          oDiv.style.bottom = oldTop - (oEv.clientY + oldY) + 'px'
        } else if (obj.className == 'l') {
          oDiv.style.height = oldHeight + 'px'
          oDiv.style.width = oldWidth - (oEv.clientX - oldX) + 'px'
          oDiv.style.left = oldLeft + (oEv.clientX - oldX) + 'px'
        } else if (obj.className == 'r') {
          oDiv.style.height = oldHeight + 'px'
          oDiv.style.width = oldWidth + (oEv.clientX - oldX) + 'px'
          oDiv.style.right = oldLeft - (oEv.clientX - oldX) + 'px'
        }
      }
      document.onmouseup = function() {
        document.onmousemove = null
      }
      return false
    }
  }
}

function draggable(ele) {
  const dialogHeaderEl = ele.querySelector('.drag')
  const dragDom = ele
  const sty = dragDom.currentStyle || window.getComputedStyle(dragDom, null)
  dialogHeaderEl.onmousedown = (e) => {
  e.preventDefault()
    // 鼠标按下，计算当前元素距离可视区的距离
    const disX = e.clientX - dialogHeaderEl.offsetLeft
    const disY = e.clientY - dialogHeaderEl.offsetTop
    // 获取到的值带px 正则匹配替换
    let styL, styT
    // 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
    if (sty.left.includes('%')) {
      styL = +document.body.clientWidth * (+sty.left.replace(/\%/g, '') / 100)
      styT = +document.body.clientHeight * (+sty.top.replace(/\%/g, '') / 100)
    } else {
      styL = +sty.left.replace(/\px/g, '')
      styT = +sty.top.replace(/\px/g, '')
    }
    document.onmousemove = function(e) {
      // 通过事件委托，计算移动的距离
      const l = e.clientX - disX
      const t = e.clientY - disY

      const ele = document.getElementById('designer')
      const targets = ele.querySelectorAll('.wtHider')
      let h = parseInt(targets[0].style.height)
      let w = parseInt(targets[0].style.width)
      // 移动当前元素
      if(t + styT + dragDom.offsetHeight < h) {
        dragDom.style.top = `${t + styT}px`
      }
      if(l + styL + dragDom.offsetWidth < w) {
        dragDom.style.left = `${l + styL}px`
      }
    }
    document.onmouseup = function(e) {
      document.onmousemove = null
      document.onmouseup = null
    }
  }
}
