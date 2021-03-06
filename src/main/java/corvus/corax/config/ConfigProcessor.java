/**
 * Copyright (c) 2010-2014 Corvus Corax Entertainment
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Corvus Corax Entertainment nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package corvus.corax.config;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import corvus.corax.Corax;
import corvus.corax.CoraxProcessor;
import corvus.corax.Describer;
import corvus.corax.util.Tools;

/**
 * @author Vlad
 *
 */
public class ConfigProcessor implements CoraxProcessor {
	private static final Logger log = Logger.getLogger(ConfigProcessor.class.getName());
	
	@Override
	public void process(Describer describer, Corax corax) {
		try { // Inject annotations
			CorvusConfig config = Corax.config();
			
			Field[] fields = Tools.getFieldsWithAnnotation(Config.class, describer.target);
			for(Field field : fields) {
				
				Config anno = field.getAnnotation(Config.class);

				Object value = config.getProperty(anno.key(), Tools.parsePrimitiveTypes(field.getType(), anno.value()));
				
				field.set(describer.value, value);

				if(anno.subscribe())
					config.addSubscriber(anno.key(), describer.value, field);
			}
			
		}
		catch (Exception e) {
			log.log(Level.SEVERE, "Faild processing Config annotations.", e);
		}
	}

	@Override
	public boolean isInitializer() {
		return false;
	}
	
}
