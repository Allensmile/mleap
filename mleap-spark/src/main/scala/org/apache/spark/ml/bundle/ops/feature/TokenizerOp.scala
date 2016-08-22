package org.apache.spark.ml.bundle.ops.feature

import ml.bundle.Bundle
import ml.bundle.op.{OpModel, OpNode}
import ml.bundle.serializer.BundleContext
import ml.bundle.wrapper.{ReadableModel, ReadableNode, Shape, WritableModel}
import org.apache.spark.ml.feature.Tokenizer

/**
  * Created by hollinwilkins on 8/21/16.
  */
object TokenizerOp extends OpNode[Tokenizer, Tokenizer] {
  override val Model: OpModel[Tokenizer] = new OpModel[Tokenizer] {
    override def opName: String = Bundle.BuiltinOps.feature.tokenizer

    override def store(context: BundleContext, model: WritableModel, obj: Tokenizer): Unit = { }

    override def load(context: BundleContext, model: ReadableModel): Tokenizer = new Tokenizer(uid = "")
  }

  override def name(node: Tokenizer): String = node.uid

  override def model(node: Tokenizer): Tokenizer = node

  override def load(context: BundleContext, node: ReadableNode, model: Tokenizer): Tokenizer = {
    new Tokenizer(uid = node.name)
  }

  override def shape(node: Tokenizer): Shape = Shape().withStandardIO(node.getInputCol, node.getOutputCol)
}
