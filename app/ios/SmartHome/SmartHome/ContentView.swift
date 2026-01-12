//
//  ContentView.swift
//  SmartHome
//
//  Created by Niels Marsman on 11/01/2026.
//

import SwiftUI
import SmartHomeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        PlatformKt.mainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // No-op (for now)
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
    }
}

#Preview {
    ContentView()
}
