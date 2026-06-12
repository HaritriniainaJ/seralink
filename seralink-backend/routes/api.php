<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\JobController;
use App\Http\Controllers\Api\ProposalController;
use App\Http\Controllers\Api\MessageController;
use App\Http\Controllers\Api\DashboardController;
use App\Http\Controllers\Api\ContractController;
use App\Http\Controllers\Api\ProfileController;

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

    // Proposals
    Route::get('/jobs/{jobId}/proposals', [ProposalController::class, 'index']);
    Route::post('/jobs/{jobId}/proposals', [ProposalController::class, 'store']);
    Route::post('/proposals/{id}/accept', [ProposalController::class, 'accept']);
    Route::post('/proposals/{id}/reject', [ProposalController::class, 'reject']);
    Route::get('/my-proposals', [ProposalController::class, 'myProposals']);

    // Dashboard
    Route::get('/dashboard/client', [DashboardController::class, 'client']);
    Route::get('/dashboard/freelance', [DashboardController::class, 'freelance']);

    // Profile
    Route::get('/profile', [ProfileController::class, 'show']);
    Route::put('/profile', [ProfileController::class, 'update']);
    Route::get('/users/{id}', [ProfileController::class, 'publicProfile']);

    // Contracts (routes spécifiques AVANT les routes avec {id})
    Route::get('/contracts', [ContractController::class, 'index']);
    Route::get('/contracts/{contractId}/messages', [MessageController::class, 'index']);
    Route::post('/contracts/{contractId}/messages', [MessageController::class, 'store']);
    Route::post('/contracts/{id}/pay', [ContractController::class, 'pay']);
    Route::post('/contracts/{id}/complete', [ContractController::class, 'complete']);
    Route::post('/contracts/{id}/release', [ContractController::class, 'release']);
    Route::post('/contracts/{id}/dispute', [ContractController::class, 'dispute']);
    Route::get('/contracts/{id}/pdf', [ContractController::class, 'generatePdf']);
    Route::post('/contracts/{id}/sign', [ContractController::class, 'sign']);
    Route::get('/contracts/{id}', [ContractController::class, 'show']);
});