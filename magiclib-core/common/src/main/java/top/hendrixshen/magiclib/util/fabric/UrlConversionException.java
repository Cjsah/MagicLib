/*
 * Copyright 2016 FabricMC
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

package top.hendrixshen.magiclib.util.fabric;

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/1a833267b54beea5eb635222df4af149f8a6a1d6/src/main/java/net/fabricmc/loader/impl/util/UrlConversionException.java">FabricLoader<a/>
 */
public class UrlConversionException extends Exception {
    public UrlConversionException() {
        super();
    }

    public UrlConversionException(String s) {
        super(s);
    }

    public UrlConversionException(Throwable t) {
        super(t);
    }

    public UrlConversionException(String s, Throwable t) {
        super(s, t);
    }
}
