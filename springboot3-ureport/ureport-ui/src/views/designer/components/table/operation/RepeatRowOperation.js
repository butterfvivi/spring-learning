import { renderRowHeader } from '../HeaderUtils.js'

export function doRepeatRow(type) {
  const selected = this.getSelected()
  const startRow = selected[0]
  const endRow = selected[2]
  const context = this.context
  for (let rowNumber = startRow; rowNumber <= endRow; rowNumber++) {
    if(type === 'cancel') {
       context.adjustDelRowHeaders(rowNumber)
    } else {
      context.addRowHeader(rowNumber, type)
    }
  }
  renderRowHeader(this, context)
}
