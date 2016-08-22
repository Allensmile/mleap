package org.apache.spark.ml.bundle.ops.feature

import ml.bundle.Bundle
import ml.bundle.op.{OpModel, OpNode}
import ml.bundle.serializer.BundleContext
import ml.bundle.wrapper._
import org.apache.spark.ml.feature.StringIndexerModel

/**
  * Created by hollinwilkins on 8/21/16.
  */
object StringIndexerOp extends OpNode[StringIndexerModel, StringIndexerModel] {
  override val Model: OpModel[StringIndexerModel] = new OpModel[StringIndexerModel] {
    override def opName: String = Bundle.BuiltinOps.feature.string_indexer

    override def store(context: BundleContext, model: WritableModel, obj: StringIndexerModel): Unit = {
      model.withAttr(Attribute.stringList("labels", obj.labels))
    }

    override def load(context: BundleContext, model: ReadableModel): StringIndexerModel = {
      new StringIndexerModel(uid = "", labels = model.attr("labels").getStringList.toArray)
    }
  }

  override def name(node: StringIndexerModel): String = node.uid

  override def model(node: StringIndexerModel): StringIndexerModel = node

  override def load(context: BundleContext, node: ReadableNode, model: StringIndexerModel): StringIndexerModel = {
    new StringIndexerModel(uid = node.name, labels = model.labels)
  }

  override def shape(node: StringIndexerModel): Shape = Shape().withStandardIO(node.getInputCol, node.getOutputCol)
}
