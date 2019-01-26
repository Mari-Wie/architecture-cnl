package org.architecture.cnl.ide;

import java.util.List;

import org.architecture.cnl.ArchitectureCNLStandaloneSetup;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

public class CNL2OWLGenerator {

	public void transformCNLFile(String path) {
		ArchitectureCNLStandaloneSetup setup = new ArchitectureCNLStandaloneSetup();

		Injector injector = setup.createInjectorAndDoEMFRegistration();

		XtextResourceSet set = injector.getInstance(XtextResourceSet.class);
		// load a resource by URI, in this case from the file system
		Resource resource = set.getResource(URI.createFileURI(path), true);
		IResourceValidator validator = ((XtextResource) resource).getResourceServiceProvider().getResourceValidator();
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		for (Issue issue : issues) {
			System.out.println(issue.getMessage());
		}
		GeneratorDelegate generator = injector.getInstance(GeneratorDelegate.class);

		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		System.out.println(resource);
		generator.doGenerate(resource, fsa);
	}

}
