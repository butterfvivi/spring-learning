/**
 * Created by Jacky.Gao on 2017-03-22.
 */

export function renderRowHeader(hot,context){
    const countRows=hot.countRows();
    const headers=[];
    const rowHeaders=context.rowHeaders;
    for(let i=1;i<=countRows;i++){
        let type='';
        for(let header of rowHeaders){
            if(header.rowNumber===(i-1)){
                if(header.band==='headerrepeat'){
                    type=`<span style='position: absolute;right:0;color:#409EFF;font-size: 10px' title='重复表头:分页时每页头部重复显示'>H</span>`;
                }else if(header.band==='footerrepeat'){
                    type=`<span style='position: absolute;right:0;color:#409EFF;font-size: 10px' title='重复表尾:分页时每页尾部重复显示'>F</span>`;
                }else if(header.band==='title'){
                    type=`<span style='position: absolute;right:0;color:#409EFF;font-size: 10px' title='标题行:分页时只会出现在第一页第一行的行'>T</span>`;
                }else if(header.band==='summary'){
                    type=`<span style='position: absolute;right:0;color:#409EFF;font-size: 10px' title='总结行:分页输出时最后一页的最下端显示'>S</span>`;
                }
                break;
            }
        }
        headers.push(i+type);
    }
    hot.updateSettings({
        rowHeaders:headers
    });
};

/**
 * 插入行时调整行类型
 * @param {number} row 插入行索引
 * @param {number} rowNum 插入行数
 */
export function adjustInsertRowHeaders(context, row, rowNum = 1) {
  const rowHeaders = context.rowHeaders;
  for (let header of rowHeaders) {
    if (header.rowNumber >= row) {
      header.rowNumber += rowNum;
    }
  }
}

/**
 * 删除行时调整行类型
 * @param {number} row 删除行索引
 * @param {number} rowNum 删除行数
 */
export function adjustDelRowHeaders(context, row, rowNum = 1) {
  const rowHeaders = context.rowHeaders;
  for (let i = 0; i < rowHeaders.length; i++) {
    let header = rowHeaders[i];
    if (header.rowNumber >= row) {
      if (header.rowNumber < row + rowNum) {
        rowHeaders.splice(i, 1);
        i--;
        continue;
      }
      header.rowNumber -= rowNum;
    }
  }
}