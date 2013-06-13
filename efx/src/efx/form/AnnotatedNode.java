/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package efx.form;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class AnnotatedNode extends StackPane  
{
    private ObjectProperty<Node> content;
    private ObservableList<Node> annotations;

    public AnnotatedNode()
    {
        this(null);
    }

    public AnnotatedNode(Node content)
    {
        this.content = new SimpleObjectProperty<Node>();
        this.content.addListener(new ChangeListener<Node>()
        {
            public void changed(ObservableValue<? extends Node> source, Node oldValue, Node newValue)
            {
                if (oldValue != null)
                {
                    getChildren().remove(oldValue);
                }
                if (newValue != null)
                {
                    getChildren().add(0, newValue);
                }
            }
        });
        setContent(content);

        this.annotations = FXCollections.observableArrayList();
        this.annotations.addListener(new ListChangeListener<Node>()
        {
            public void onChanged(Change<? extends Node> change)
            {
                getChildren().clear();
                Node content = AnnotatedNode.this.content.get();
                if (content != null)
                {
                    getChildren().add(content);
                }
                getChildren().addAll(annotations);
            }
        });
        
        
    }

    public ObjectProperty<Node> contentProperty()
    {
        return content;
    }

    public Node getContent()
    {
        return content.get();
    }

    public void setContent(Node content)
    {
        this.content.set(content);
    }

    public ObservableList<Node> getAnnotations()
    {
        return annotations;
    }

    protected void layoutChildren()
    {
        double width = getWidth();
        double height = getHeight();

        Node node = content.get();
        if (node != null)
        {
            node.resizeRelocate(0, 0, width, height);
        }

        for (Node annotation : annotations)
        {
            Bounds annotationBounds = annotation.getLayoutBounds();
            annotation.relocate(
                    width - annotationBounds.getWidth() + annotation.getTranslateX(),
                    height - annotationBounds.getHeight() + annotation.getTranslateY());
        }
    }

    protected double computeMaxHeight(double d)
    {
        Node node = content.get();
        return node == null ? super.computeMaxHeight(d) : node.maxHeight(d);
    }

    protected double computeMinHeight(double d)
    {
        Node node = content.get();
        return node == null ? super.computeMinHeight(d) : node.minHeight(d);
    }

    protected double computePrefHeight(double d)
    {
        Node node = content.get();
        return node == null ? super.computePrefHeight(d) : node.prefHeight(d);
    }

    protected double computePrefWidth(double d)
    {
        Node node = content.get();
        return node == null ? super.computePrefWidth(d) : node.prefWidth(d);
    }

    protected double computeMaxWidth(double d)
    {
        Node node = content.get();
        return node == null ? super.computeMaxWidth(d) : node.maxWidth(d);
    }

    protected double computeMinWidth(double d)
    {
        Node node = content.get();
        return node == null ? super.computeMinWidth(d) : node.minWidth(d);
    }
}
