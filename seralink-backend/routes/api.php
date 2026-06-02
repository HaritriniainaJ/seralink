<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\JobController;
use App\Http\Controllers\Api\ProposalController;
use App\Http\Controllers\Api\MessageController;
use App\Http\Controllers\Api\DashboardController;

// Routes publiques
Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);

// Routes publiques jobs
Route::get('/jobs', [JobController::class, 'index']);
Route::get('/jobs/{id}', [JobController::class, 'show']);

// Routes protégées
Route::middleware('auth:sanctum')->group(function () {

    // Auth
    Route::post('/logout', [AuthController::class, 'logout']);

    // Jobs (client)
    Route::post('/jobs', [JobController::class, 'store']);
    Route::put('/jobs/{id}', [JobController::class, 'update']);
    Route::delete('/jobs/{id}', [JobController::class, 'destroy']);

    // Proposals (freelance)
    Route::get('/jobs/{jobId}/proposals', [ProposalController::class, 'index']);
    Route::post('/jobs/{jobId}/proposals', [ProposalController::class, 'store']);
    Route::post('/proposals/{id}/accept', [ProposalController::class, 'accept']);
    Route::post('/proposals/{id}/reject', [ProposalController::class, 'reject']);
    Route::get('/my-proposals', [ProposalController::class, 'myProposals']);

    // Messages
    Route::get('/contracts/{contractId}/messages', [MessageController::class, 'index']);
    Route::post('/contracts/{contractId}/messages', [MessageController::class, 'store']);

    // Dashboard
    Route::get('/dashboard/client', [DashboardController::class, 'client']);
    Route::get('/dashboard/freelance', [DashboardController::class, 'freelance']);
});