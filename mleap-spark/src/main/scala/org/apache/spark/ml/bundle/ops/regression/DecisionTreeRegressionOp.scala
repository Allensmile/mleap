package org.apache.spark.ml.bundle.ops.regression

import ml.bundle.Bundle
import ml.bundle.op.{OpModel, OpNode}
import ml.bundle.serializer.BundleContext
import ml.bundle.tree.TreeSerializer
import ml.bundle.wrapper._
import org.apache.spark.ml.bundle.tree.SparkNodeWrapper
import org.apache.spark.ml.regression.DecisionTreeRegressionModel

/**
  * Created by hollinwilkins on 8/22/16.
  */
object DecisionTreeRegressionOp extends OpNode[DecisionTreeRegressionModel, DecisionTreeRegressionModel] {
  implicit val nodeWrapper = SparkNodeWrapper

  override val Model: OpModel[DecisionTreeRegressionModel] = new OpModel[DecisionTreeRegressionModel] {
    override def opName: String = Bundle.BuiltinOps.regression.decision_tree_regression

    override def store(context: BundleContext, model: WritableModel, obj: DecisionTreeRegressionModel): Unit = {
      model.withAttr(Attribute.long("num_features", obj.numFeatures))
      TreeSerializer[org.apache.spark.ml.tree.Node](context.file("nodes"), withImpurities = false).write(obj.rootNode)
    }

    override def load(context: BundleContext, model: ReadableModel): DecisionTreeRegressionModel = {
      val rootNode = TreeSerializer[org.apache.spark.ml.tree.Node](context.file("nodes"), withImpurities = false).read().asInstanceOf[org.apache.spark.ml.tree.Node]
      new DecisionTreeRegressionModel(uid = "",
        rootNode = rootNode,
        numFeatures = model.attr("num_features").getLong.toInt)
    }
  }

  override def name(node: DecisionTreeRegressionModel): String = node.uid

  override def model(node: DecisionTreeRegressionModel): DecisionTreeRegressionModel = node

  override def load(context: BundleContext, node: ReadableNode, model: DecisionTreeRegressionModel): DecisionTreeRegressionModel = {
    new DecisionTreeRegressionModel(uid = node.name,
      rootNode = model.rootNode,
      numFeatures = model.numFeatures).
      setFeaturesCol(node.shape.input("features").name).
      setPredictionCol(node.shape.output("prediction").name)
  }

  override def shape(node: DecisionTreeRegressionModel): Shape = Shape().withInput(node.getFeaturesCol, "features").
    withOutput(node.getPredictionCol, "prediction")
}
